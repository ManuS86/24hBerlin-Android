package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.data.model.Settings
import com.example.a24hberlin.data.repository.UserRepositoryImpl
import com.example.a24hberlin.services.AndroidReminderScheduler
import com.example.a24hberlin.utils.checkPassword
import com.example.a24hberlin.utils.toLanguageOrNull
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val userRepo = UserRepositoryImpl(db)
    private val notificationService = AndroidReminderScheduler(application.applicationContext)

    val confirmationMessage = savedStateHandle.getStateFlow("confirmationMessage", null as Int?)

    val firebaseError = savedStateHandle.getStateFlow("firebaseError", null as String?)

    var language by mutableStateOf<Language?>(null)
        private set

    val passwordError = savedStateHandle.getStateFlow("passwordError", null as Int?)

    var pushNotificationsEnabled = savedStateHandle.getStateFlow("pushNotificationsEnabled", true)

    var isReauthenticated = savedStateHandle.getStateFlow("isReauthenticated", false)
        private set

    private var currentAppUser by mutableStateOf<AppUser?>(null)

    init {
        if (listener == null) {
            listener = userRepo.addUserListener { user ->
                currentAppUser = user
                savedStateHandle["pushNotificationsEnabled"] =
                    user?.settings?.pushNotificationsEnabled ?: false
                language = user?.settings?.language?.toLanguageOrNull()
            }
        }
    }

    fun changeEmail(email: String) {
        viewModelScope.launch {
            try {
                userRepo.changeEmail(email)
                savedStateHandle["firebaseError"] = null
                savedStateHandle["confirmationMessage"] = R.string.email_changed_successfully
            } catch (ex: Exception) {
                savedStateHandle["confirmationMessage"] = null
                savedStateHandle["firebaseError"] = ex.localizedMessage
                Log.e("Change Email", ex.toString())
            }
        }
    }

    fun changePassword(password: String, confirmPassword: String) {
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null

        savedStateHandle["passwordError"] = checkPassword(password, confirmPassword)

        if (passwordError == null) {
            viewModelScope.launch {
                try {
                    userRepo.changePassword(password)
                    savedStateHandle["passwordError"] = null
                    savedStateHandle["confirmationMessage"] = R.string.password_changed_successfully
                } catch (ex: Exception) {
                    savedStateHandle["confirmationMessage"] = null
                    savedStateHandle["firebaseError"] = ex.localizedMessage
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
        savedStateHandle["firebaseError"] = null

        viewModelScope.launch {
            try {
                userRepo.reAuthenticate(password)
                savedStateHandle["isReauthenticated"] = true
            } catch (ex: Exception) {
                savedStateHandle["firebaseError"] = ex.localizedMessage
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

    fun removeAllPendingNotifications(favorites: List<Event>) {
        viewModelScope.launch {
            notificationService.cancelAllPendingReminders(favorites)
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
        savedStateHandle["pushNotificationsEnabled"] = it
        saveSettings()
    }

    fun clearErrorMessages() {
        savedStateHandle["confirmationMessage"] = null
        savedStateHandle["passwordError"] = null
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null
    }

    fun updateLanguage(it: Language?) {
        language = it
        saveSettings()
    }

    private fun saveSettings() {
        val settings = Settings(
            pushNotificationsEnabled = pushNotificationsEnabled.value,
            language = language?.label
        )

        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(null, settings)
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