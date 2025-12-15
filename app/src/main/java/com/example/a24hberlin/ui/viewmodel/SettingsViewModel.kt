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
import com.example.a24hberlin.notifications.schedule.AndroidReminderScheduler
import com.example.a24hberlin.utils.checkPassword
import com.example.a24hberlin.utils.toLanguageOrNull
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "SettingsViewModel"

class SettingsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val userRepo = UserRepositoryImpl(db)
    private val notificationService = AndroidReminderScheduler(application.applicationContext)

    val confirmationMessage = savedStateHandle.getStateFlow("confirmationMessage", null as Int?)
    val firebaseError = savedStateHandle.getStateFlow("firebaseError", null as String?)
    val passwordError = savedStateHandle.getStateFlow("passwordError", null as Int?)

    var isReauthenticated = savedStateHandle.getStateFlow("isReauthenticated", false)
        private set

    private val _language = MutableStateFlow<Language?>(null)
    val language: StateFlow<Language?> = _language.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    private var firebaseListener: ListenerRegistration? = null
    private var currentAppUser: AppUser? = null

    init {
        if (firebaseListener == null) {
            firebaseListener = userRepo.addUserListener { user ->
                currentAppUser = user

                savedStateHandle["pushNotificationsEnabled"] =
                    user?.settings?.pushNotificationsEnabled ?: false

                _language.value = user?.settings?.language?.toLanguageOrNull()
            }
        }
    }

    var pushNotificationsEnabledState =
        savedStateHandle.getStateFlow("pushNotificationsEnabled", true)

    fun changeLanguage(newLanguage: Language?) {
        val settings = Settings(
            pushNotificationsEnabled = pushNotificationsEnabledState.value,
            language = newLanguage?.label
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.updateUserInformation(null, settings)
            } catch (ex: Exception) {
                Log.e(TAG, "Error changing language.", ex)
            }
        }
    }

    fun changePushNotifications(enabled: Boolean) {
        val settings = Settings(
            pushNotificationsEnabled = enabled,
            language = _language.value?.label
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.updateUserInformation(null, settings)
            } catch (ex: Exception) {
                Log.e(TAG, "Error changing push notifications setting.", ex)
            }
        }
    }

    fun removeAllPendingNotifications(favorites: List<Event>) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationService.cancelAllPendingReminders(favorites)
        }
    }

    fun changeEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.changeEmail(email)
                savedStateHandle["firebaseError"] = null
                savedStateHandle["confirmationMessage"] = R.string.email_changed_successfully
            } catch (ex: Exception) {
                savedStateHandle["confirmationMessage"] = null
                savedStateHandle["firebaseError"] = ex.localizedMessage
                Log.e(TAG, "Error changing email.", ex)
            }
        }
    }

    fun changePassword(password: String, confirmPassword: String) {
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null

        val errorResId = checkPassword(password, confirmPassword)

        savedStateHandle["passwordError"] = errorResId

        if (errorResId == null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    userRepo.changePassword(password)
                    savedStateHandle["passwordError"] = null
                    savedStateHandle["confirmationMessage"] = R.string.password_changed_successfully
                } catch (ex: Exception) {
                    savedStateHandle["confirmationMessage"] = null
                    savedStateHandle["firebaseError"] = ex.localizedMessage
                    Log.e(TAG, "Error changing password.", ex)
                }
            }
        }
    }

    fun reAuthenticate(password: String) {
        savedStateHandle["firebaseError"] = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.reAuthenticate(password)
                savedStateHandle["isReauthenticated"] = true
            } catch (ex: Exception) {
                savedStateHandle["firebaseError"] = ex.localizedMessage
                Log.e(TAG, "Error during re-authentication.", ex)
            }
        }
    }

    fun logout() {
        try {
            userRepo.logout()
        } catch (ex: Exception) {
            Log.e(TAG, "Error during logout.", ex)
        }
    }

    fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.deleteUserDataAndAuth()
            } catch (ex: Exception) {
                Log.e(TAG, "Error during account deletion.", ex)
            }
        }
    }

    fun sendBugReport(message: String, completion: (Exception?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepo.sendBugReport(message, completion)
            } catch (ex: Exception) {
                Log.e(TAG, "Error sending bug report.", ex)
                completion(ex)
            }
        }
    }

    fun clearErrorMessages() {
        savedStateHandle["confirmationMessage"] = null
        savedStateHandle["firebaseError"] = null
        savedStateHandle["passwordError"] = null
    }

    override fun onCleared() {
        super.onCleared()
        firebaseListener?.remove()
        firebaseListener = null
    }
}