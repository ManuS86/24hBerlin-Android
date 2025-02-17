package com.example.a24hberlin.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Settings
import com.example.a24hberlin.data.repository.UserRepository
import com.example.a24hberlin.utils.checkPassword
import com.example.a24hberlin.utils.toLanguageOrNull
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val TAG = "SettingsViewModel"
    private val userRepo = UserRepository(db)

    var confirmationMessage by mutableStateOf<String?>(null)
        private set

    var currentAppUser by mutableStateOf<AppUser?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var language by mutableStateOf<Language?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var pushNotificationsEnabled by mutableStateOf(false)
        private set

    var isReauthenticated by mutableStateOf(false)
        private set

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
                errorMessage = null
                confirmationMessage = "email_changed_successfully."
            } catch (ex: Exception) {
                confirmationMessage = null
                errorMessage = ex.localizedMessage
                Log.e("Change Email", ex.toString())
            }
        }
    }

    fun changePassword(password: String, confirmPassword: String) {
        errorMessage = null
        passwordError = null

        passwordError = checkPassword(password, confirmPassword)

        if (errorMessage == null && passwordError == null) {
            viewModelScope.launch {
                try {
                    userRepo.changePassword(password)
                    errorMessage = null
                    passwordError = null
                    confirmationMessage = "password_changed_successfully."
                } catch (ex: Exception) {
                    confirmationMessage = null
                    errorMessage = ex.localizedMessage
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
        errorMessage = null

        viewModelScope.launch {
            try {
                userRepo.reAuthenticate(password)
                isReauthenticated = true
            } catch (ex: Exception) {
                errorMessage = ex.localizedMessage
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
        // Implementation for removing notifications
    }

    fun saveSettings() {
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

    fun sendBugReport(message: String, completion: (Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                userRepo.sendBugReport(message, completion)
            } catch (ex: Exception) {
                Log.e("Send Bug Report", ex.toString())
            }
        }
    }

    fun clearErrorMessages() {
        confirmationMessage = null
        errorMessage = null
        passwordError = null
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
        listener = null
    }
}