package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Settings
import com.example.a24hberlin.data.repository.UserRepositoryImpl
import com.example.a24hberlin.services.NotificationService
import com.example.a24hberlin.utils.checkPassword
import com.example.a24hberlin.utils.toLanguageOrNull
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val userRepo = UserRepositoryImpl(db)
    private val notificationService = NotificationService(application.applicationContext)

    var confirmationMessage by mutableStateOf<Int?>(null)
        private set

    var firebaseErrorMessage by mutableStateOf<String?>(null)
        private set

    var language by mutableStateOf<Language?>(null)
        private set

    var passwordError by mutableStateOf<Int?>(null)
        private set

    var pushNotificationsEnabled by mutableStateOf(false)
        private set

    var isReauthenticated by mutableStateOf(false)
        private set

    private var currentAppUser by mutableStateOf<AppUser?>(null)

    init {
        if (listener == null) {
            listener = userRepo.addUserListener { user ->
                currentAppUser = user
                pushNotificationsEnabled = user?.settings?.pushNotificationsEnabled ?: false
                language = user?.settings?.language?.toLanguageOrNull()
            }
        }
    }

    fun changeEmail(email: String) {
        viewModelScope.launch {
            try {
                userRepo.changeEmail(email)
                firebaseErrorMessage = null
                confirmationMessage = R.string.email_changed_successfully
            } catch (ex: Exception) {
                confirmationMessage = null
                firebaseErrorMessage = ex.localizedMessage
                Log.e("Change Email", ex.toString())
            }
        }
    }

    fun changePassword(password: String, confirmPassword: String) {
        firebaseErrorMessage = null
        passwordError = null

        passwordError = checkPassword(password, confirmPassword)

        if (passwordError == null && firebaseErrorMessage == null) {
            viewModelScope.launch {
                try {
                    userRepo.changePassword(password)
                    passwordError = null
                    confirmationMessage = R.string.password_changed_successfully
                } catch (ex: Exception) {
                    confirmationMessage = null
                    firebaseErrorMessage = ex.localizedMessage
                    Log.e("Change Password", ex.toString())
                }
            }
        }
    }

    fun logout() {
        try {
            userRepo.logout()
        } catch (ex: Exception) {
            Log.e("Logout", ex.toString())
        }
    }

    fun reAuthenticate(password: String) {
        firebaseErrorMessage = null

        viewModelScope.launch {
            try {
                userRepo.reAuthenticate(password)
                isReauthenticated = true
            } catch (ex: Exception) {
                firebaseErrorMessage = ex.localizedMessage
                Log.e("Re-Authentication", ex.toString())
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                userRepo.deleteUserDataAndAuth()
            } catch (ex: Exception) {
                Log.e("Account Deletion", ex.toString())
            }
        }
    }

    fun removeAllPendingNotifications() {
        viewModelScope.launch {
            notificationService.removeAllPendingNotifications()
        }
    }

    fun sendBugReport(message: String, completion: (Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                userRepo.sendBugReport(message, completion)
            } catch (ex: Exception) {
                Log.e("Send Bug Report", ex.toString())
            }
        }
    }

    fun changePushNotifications(it: Boolean) {
        pushNotificationsEnabled = it
        saveSettings()
    }

    fun clearErrorMessages() {
        confirmationMessage = null
        firebaseErrorMessage = null
        passwordError = null
    }

    fun updateLanguage(it: Language?) {
        language = it
        saveSettings()
    }

    private fun saveSettings() {
        val settings = Settings(
            pushNotificationsEnabled = pushNotificationsEnabled,
            language = language?.label
        )

        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(settings = settings)
            } catch (ex: Exception) {
                Log.e("Save Settings", ex.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
        listener = null
    }
}