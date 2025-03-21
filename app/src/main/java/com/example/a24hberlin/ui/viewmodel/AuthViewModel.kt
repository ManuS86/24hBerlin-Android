package com.example.a24hberlin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.R
import com.example.a24hberlin.data.repository.UserRepositoryImpl
import com.example.a24hberlin.utils.checkPassword
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private var analytics: FirebaseAnalytics
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private val userRepo = UserRepositoryImpl(db)

    val confirmationMessage = savedStateHandle.getStateFlow("confirmationMessage", null as Int?)

    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    val errorMessage = savedStateHandle.getStateFlow("errorMessage", null as Int?)

    val firebaseError = savedStateHandle.getStateFlow("firebaseError", null as String?)

    val passwordError = savedStateHandle.getStateFlow("passwordError", null as Int?)

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }

        analytics = Firebase.analytics
    }

    fun register(email: String, password: String, confirmPassword: String) {
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null

        savedStateHandle["passwordError"] = checkPassword(password, confirmPassword)

        if (passwordError == null) {
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
        savedStateHandle["confirmationMessage"] = null
        savedStateHandle["errorMessage"] = null
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null
    }
}