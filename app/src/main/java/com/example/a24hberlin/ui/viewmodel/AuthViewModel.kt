package com.example.a24hberlin.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.R
import com.example.a24hberlin.data.repository.UserRepository
import com.example.a24hberlin.utils.checkPassword
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private var analytics: FirebaseAnalytics
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "AuthViewModel"
    private val userRepo = UserRepository(db)

    var confirmationMessage by mutableStateOf<Int?>(null)
        private set

    var currentUser by mutableStateOf(auth.currentUser)
        private set

    var errorMessage by mutableStateOf<Int?>(null)
        private set

    var firebaseErrorMessage by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<Int?>(null)
        private set

    init {
        auth.addAuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
        }

        analytics = Firebase.analytics
    }

    fun register(email: String, password: String, confirmPassword: String) {
        errorMessage = null
        firebaseErrorMessage = null
        passwordError = null

        passwordError = checkPassword(password, confirmPassword)

        if (errorMessage == null && passwordError == null && firebaseErrorMessage == null) {
            viewModelScope.launch {
                try {
                    userRepo.register(email, password)
                    auth.useAppLanguage()
                    auth.currentUser?.sendEmailVerification()
                } catch (ex: Exception) {
                    firebaseErrorMessage = ex.localizedMessage
                    Log.e("Registration", ex.toString())
                }
            }
        }
    }

    fun login(email: String, password: String) {
        errorMessage = null

        viewModelScope.launch {
            try {
                userRepo.login(email, password)
                analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                    param(FirebaseAnalytics.Param.ITEM_ID, currentUser?.email!!)
                    param(FirebaseAnalytics.Param.ITEM_NAME, "Email")
                }
            } catch (ex: Exception) {
                errorMessage = R.string.invalid_email_or_password
                Log.e("Login", ex.toString())
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                userRepo.resetPassword(email)
                errorMessage = null
                firebaseErrorMessage = null
                confirmationMessage = R.string.email_sent
            } catch (ex: Exception) {
                confirmationMessage = null
                firebaseErrorMessage = ex.localizedMessage
                Log.e("Password reset requested", ex.toString())
            }
        }
    }

    fun clearErrorMessages() {
        confirmationMessage = null
        errorMessage = null
        firebaseErrorMessage = null
        passwordError = null
    }
}