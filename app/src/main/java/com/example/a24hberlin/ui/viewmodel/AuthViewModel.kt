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
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "AuthViewModel"
        private const val KEY_CONFIRMATION_MESSAGE = "confirmationMessage"
        private const val KEY_ERROR_MESSAGE = "errorMessage"
        private const val KEY_FIREBASE_ERROR = "firebaseError"
        private const val KEY_PASSWORD_ERROR = "passwordError"
    }

    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private val userRepo = UserRepositoryImpl(db)
    private var analytics: FirebaseAnalytics = Firebase.analytics
    private val permissionManager = AndroidPermissionManager(application)

    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    val hasNotificationPermission: StateFlow<Boolean> =
        permissionManager.hasNotificationPermission.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = permissionManager.hasNotificationPermission.value
        )

    val confirmationMessageResId = savedStateHandle.getStateFlow(KEY_CONFIRMATION_MESSAGE, null as Int?)
    val errorMessageResId = savedStateHandle.getStateFlow(KEY_ERROR_MESSAGE, null as Int?)
    val firebaseError = savedStateHandle.getStateFlow(KEY_FIREBASE_ERROR, null as String?)
    val passwordErrorResId = savedStateHandle.getStateFlow(KEY_PASSWORD_ERROR, null as Int?)

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    fun updateNotificationPermission(isGranted: Boolean) {
        permissionManager.updateNotificationPermission(isGranted)
    }

    fun register(email: String, password: String, confirmPassword: String) {
        savedStateHandle[KEY_FIREBASE_ERROR] = null
        savedStateHandle[KEY_PASSWORD_ERROR] = null

        val errorResId = checkPassword(password, confirmPassword)

        savedStateHandle[KEY_PASSWORD_ERROR] = errorResId

        if (errorResId == null) {
            viewModelScope.launch {
                try {
                    userRepo.register(email, password)
                    auth.useAppLanguage()
                    auth.currentUser?.sendEmailVerification()
                } catch (ex: Exception) {
                    savedStateHandle[KEY_FIREBASE_ERROR] = ex.localizedMessage
                    Log.e(TAG, "Registration: ${ex.toString()}")
                }
            }
        }
    }

    fun login(email: String, password: String) {
        savedStateHandle[KEY_ERROR_MESSAGE] = null

        viewModelScope.launch {
            try {
                userRepo.login(email, password)
                analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                    param(FirebaseAnalytics.Param.ITEM_ID, currentUser.value?.email!!)
                    param(FirebaseAnalytics.Param.ITEM_NAME, "Email")
                }
            } catch (ex: Exception) {
                savedStateHandle[KEY_ERROR_MESSAGE] = R.string.invalid_email_or_password
                Log.e(TAG, "Login ${ex.toString()}")
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                userRepo.resetPassword(email)
                savedStateHandle[KEY_FIREBASE_ERROR] = null
                savedStateHandle[KEY_CONFIRMATION_MESSAGE] = R.string.email_sent
            } catch (ex: Exception) {
                savedStateHandle[KEY_CONFIRMATION_MESSAGE] = null
                savedStateHandle[KEY_FIREBASE_ERROR] = ex.localizedMessage
                Log.e(TAG, "Password reset requested: ${ex.toString()}")
            }
        }
    }

    fun clearErrorMessages() {
        clearErrorStates()
        savedStateHandle[KEY_CONFIRMATION_MESSAGE] = null
    }

    private fun clearErrorStates() {
        savedStateHandle[KEY_ERROR_MESSAGE] = null
        savedStateHandle[KEY_FIREBASE_ERROR] = null
        savedStateHandle[KEY_PASSWORD_ERROR] = null
    }
}