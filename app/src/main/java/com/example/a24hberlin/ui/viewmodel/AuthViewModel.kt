package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.repository.UserRepository
import com.example.a24hberlin.utils.checkPassword
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private var analytics: FirebaseAnalytics
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "AuthViewModel"
    private val userRepo = UserRepository(db)

    private var _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?>
        get() = _passwordError

    init {
        if (auth.currentUser != null) {
            setUpUserEnv()
        }
        analytics = Firebase.analytics
    }

    fun register(email: String, password: String, confirmPassword: String) {
        _errorMessage.value = null
        _passwordError.value = null

        _passwordError.value = checkPassword(password, confirmPassword)

        if (errorMessage == null && passwordError == null) {
            viewModelScope.launch {
                try {
                    userRepo.register(email, password)
                    auth.useAppLanguage()
                    auth.currentUser?.sendEmailVerification()
                } catch (ex: Exception) {
                    _errorMessage.value = ex.localizedMessage
                    Log.e("Registration", ex.toString())
                }
            }
        }
    }

    fun login(email: String, password: String) {
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                userRepo.login(email, password)
                analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                    param(FirebaseAnalytics.Param.ITEM_ID, _currentUser.value?.email!!)
                    param(FirebaseAnalytics.Param.ITEM_NAME, "Email")
                }
            } catch (ex: Exception) {
                _errorMessage.value = "invalid_email_or_password."
                Log.e("Login", ex.toString())
            }
        }
    }

    private fun setUpUserEnv() {
        _currentUser.value = auth.currentUser
    }
}