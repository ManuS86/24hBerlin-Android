package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.R
import com.example.a24hberlin.data.repository.UserRepositoryImpl
import com.example.a24hberlin.managers.AndroidPermissionManager
import com.example.a24hberlin.utils.checkPassword
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private val userRepo = UserRepositoryImpl(db)
    private var analytics: FirebaseAnalytics = Firebase.analytics
    private val permissionManager = AndroidPermissionManager(application)

    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    val hasNotificationPermission: StateFlow<Boolean> =
        permissionManager.hasNotificationPermission.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            permissionManager.hasNotificationPermission.value
        )

    val confirmationMessage = savedStateHandle.getStateFlow("confirmationMessage", null as Int?)
    val errorMessage = savedStateHandle.getStateFlow("errorMessage", null as Int?)
    val firebaseError = savedStateHandle.getStateFlow("firebaseError", null as String?)
    val passwordError = savedStateHandle.getStateFlow("passwordError", null as Int?)

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    fun updateNotificationPermission(isGranted: Boolean) {
        permissionManager.updateNotificationPermission(isGranted)
    }

    fun register(email: String, password: String, confirmPassword: String) {
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null

        val errorResId = checkPassword(password, confirmPassword)

        savedStateHandle["passwordError"] = errorResId

        if (errorResId == null) {
            viewModelScope.launch {
                try {
                    userRepo.register(email, password)
                    auth.useAppLanguage()
                    auth.currentUser?.sendEmailVerification()
                } catch (ex: Exception) {
                    savedStateHandle["firebaseError"] = ex.localizedMessage
                    Log.e("Registration", ex.toString())
                }
            }
        }
    }

    fun login(email: String, password: String) {
        savedStateHandle["errorMessage"] = null

        viewModelScope.launch {
            try {
                userRepo.login(email, password)
                analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                    param(FirebaseAnalytics.Param.ITEM_ID, currentUser.value?.email!!)
                    param(FirebaseAnalytics.Param.ITEM_NAME, "Email")
                }
            } catch (ex: Exception) {
                savedStateHandle["errorMessage"] = R.string.invalid_email_or_password
                Log.e("Login", ex.toString())
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                userRepo.resetPassword(email)
                savedStateHandle["firebaseError"] = null
                savedStateHandle["confirmationMessage"] = R.string.email_sent
            } catch (ex: Exception) {
                savedStateHandle["confirmationMessage"] = null
                savedStateHandle["firebaseError"] = ex.localizedMessage
                Log.e("Password reset requested", ex.toString())
            }
        }
    }

    fun clearErrorMessages() {
        clearErrorStates()
        savedStateHandle["confirmationMessage"] = null
    }

    private fun clearErrorStates() {
        savedStateHandle["errorMessage"] = null
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null
    }
}