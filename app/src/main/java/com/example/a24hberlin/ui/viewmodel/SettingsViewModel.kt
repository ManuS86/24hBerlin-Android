package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val notificationService = AndroidReminderScheduler(application.applicationContext)
    private val userRepo = UserRepositoryImpl(db)

    val confirmationMessage = savedStateHandle.getStateFlow("confirmationMessage", null as Int?)

    val firebaseError = savedStateHandle.getStateFlow("firebaseError", null as String?)

    val passwordError = savedStateHandle.getStateFlow("passwordError", null as Int?)

    var pushNotificationsEnabled = savedStateHandle.getStateFlow("pushNotificationsEnabled", true)

    var isReauthenticated = savedStateHandle.getStateFlow("isReauthenticated", false)
        private set

    private val _language = MutableStateFlow<Language?>(null)
    val language: StateFlow<Language?> = _language.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    private var currentAppUser: AppUser? = null

    init {
        if (listener == null) {
            listener = userRepo.addUserListener { user ->
                currentAppUser = user
                savedStateHandle["pushNotificationsEnabled"] =
                    user?.settings?.pushNotificationsEnabled ?: false
                _language.value = user?.settings?.language?.toLanguageOrNull()
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
        val settings = Settings(
            pushNotificationsEnabled = it,
            language = _language.value?.label
        )

        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(null, settings)
            } catch (ex: Exception) {
                Log.e("Change Push Notifications", ex.toString())
            }
        }
    }

    fun clearErrorMessages() {
        savedStateHandle["confirmationMessage"] = null
        savedStateHandle["passwordError"] = null
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null
    }

    fun changeLanguage(it: Language?) {
        val settings = Settings(
            pushNotificationsEnabled = pushNotificationsEnabled.value,
            language = it?.label
        )

        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(null, settings)
            } catch (ex: Exception) {
                Log.e("Change Language", ex.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
        listener = null
    }
}