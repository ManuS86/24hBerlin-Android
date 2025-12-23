package com.esutor.twentyfourhoursberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.enums.Language
import com.esutor.twentyfourhoursberlin.data.model.AppUser
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.data.model.Settings
import com.esutor.twentyfourhoursberlin.data.repository.user.UserRepository
import com.esutor.twentyfourhoursberlin.notifications.reminderscheduler.AndroidReminderScheduler
import com.esutor.twentyfourhoursberlin.utils.checkPassword
import com.esutor.twentyfourhoursberlin.utils.toLanguageOrNull
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val userRepo: UserRepository,
    private val reminderScheduler: AndroidReminderScheduler
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "SettingsViewModel"
        private const val KEY_CONFIRMATION_MESSAGE = "confirmationMessage"
        private const val KEY_FIREBASE_ERROR = "firebaseError"
        private const val KEY_IS_PROBLEM_REPORT_SHEET_OPEN = "isProblemReportSheetOpen"
        private const val KEY_IS_REAUTHENTICATED = "isReauthenticated"
        private const val KEY_LANGUAGE = "selectedLanguage"
        private const val KEY_PASSWORD_ERROR = "passwordError"
        private const val KEY_PROBLEM_REPORT_ALERT_MESSAGE = "problemReportAlertMessage"
        private const val KEY_NOTIFICATIONS_ENABLED = "notificationsEnabled"
        private const val KEY_SHOW_DELETE_ACCOUNT_ALERT = "showDeleteAccountAlert"
        private const val KEY_SHOW_LOGOUT_ALERT = "showLogoutAlert"
    }

    // --- UI state ---
    val confirmationMessageResId = savedStateHandle.getStateFlow(KEY_CONFIRMATION_MESSAGE, null as Int?)
    val firebaseError = savedStateHandle.getStateFlow(KEY_FIREBASE_ERROR, null as String?)
    val isProblemReportSheetOpen = savedStateHandle.getStateFlow(KEY_IS_PROBLEM_REPORT_SHEET_OPEN, false)
    val isReauthenticated = savedStateHandle.getStateFlow(KEY_IS_REAUTHENTICATED, false)
    val passwordErrorResId = savedStateHandle.getStateFlow(KEY_PASSWORD_ERROR, null as Int?)
    val problemReportAlertMessage = savedStateHandle.getStateFlow<String?>(KEY_PROBLEM_REPORT_ALERT_MESSAGE, null)
    var notificationsEnabledState = savedStateHandle.getStateFlow(KEY_NOTIFICATIONS_ENABLED, true)
    val showDeleteAccountAlert = savedStateHandle.getStateFlow(KEY_SHOW_DELETE_ACCOUNT_ALERT, false)
    val showLogoutAlert = savedStateHandle.getStateFlow(KEY_SHOW_LOGOUT_ALERT, false)
    val language = savedStateHandle.getStateFlow<Language?>(KEY_LANGUAGE, null)

    val currentLanguageCode: StateFlow<String> = language
        .map { it?.languageCode ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "")

    // --- Listeners and Cache ---
    private var firebaseListener: ListenerRegistration? = null
    private var currentAppUser: AppUser? = null

    init {
        startUserListener()
    }

    private fun startUserListener() {
        if (firebaseListener == null) {
            firebaseListener = userRepo.addUserListener { user ->
                currentAppUser = user
                savedStateHandle[KEY_NOTIFICATIONS_ENABLED] = user?.settings?.notificationsEnabled ?: false
                savedStateHandle[KEY_LANGUAGE] = user?.settings?.language?.toLanguageOrNull()
            }
        }
    }

    // --- User Settings actions ---
    fun changeLanguage(newLanguage: Language?) {
        savedStateHandle[KEY_LANGUAGE] = newLanguage

        val settings = Settings(
            notificationsEnabled = notificationsEnabledState.value,
            language = newLanguage?.label
        )

        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(null, settings)
            } catch (ex: Exception) {
                Log.e(TAG, "Error changing language.", ex)
            }
        }
    }

    fun changeNotificationPermission(enabled: Boolean) {
        val settings = Settings(
            notificationsEnabled = enabled,
            language = language.value?.label
        )

        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(null, settings)
            } catch (ex: Exception) {
                Log.e(TAG, "Error changing notification setting.", ex)
            }
        }
    }

    // --- Account and Auth actions ---
    fun reAuthenticate(password: String) {
        savedStateHandle[KEY_FIREBASE_ERROR] = null

        viewModelScope.launch {
            try {
                userRepo.reAuthenticate(password)
                savedStateHandle[KEY_IS_REAUTHENTICATED] = true
            } catch (ex: Exception) {
                savedStateHandle[KEY_FIREBASE_ERROR] = ex.localizedMessage
                Log.e(TAG, "Error during re-authentication.", ex)
            }
        }
    }

    fun changeEmail(email: String) {
        viewModelScope.launch {
            try {
                userRepo.changeEmail(email)
                savedStateHandle[KEY_FIREBASE_ERROR] = null
                savedStateHandle[KEY_CONFIRMATION_MESSAGE] = R.string.email_changed_successfully
            } catch (ex: Exception) {
                savedStateHandle[KEY_CONFIRMATION_MESSAGE] = null
                savedStateHandle[KEY_FIREBASE_ERROR] = ex.localizedMessage
                Log.e(TAG, "Error changing email.", ex)
            }
        }
    }

    fun changePassword(password: String, confirmPassword: String) {
        savedStateHandle[KEY_FIREBASE_ERROR] = null
        savedStateHandle[KEY_PASSWORD_ERROR] = null

        val errorResId = checkPassword(password, confirmPassword)

        savedStateHandle[KEY_PASSWORD_ERROR] = errorResId

        if (errorResId == null) {
            viewModelScope.launch {
                try {
                    userRepo.changePassword(password)
                    savedStateHandle[KEY_PASSWORD_ERROR] = null
                    savedStateHandle[KEY_CONFIRMATION_MESSAGE] = R.string.password_changed_successfully
                } catch (ex: Exception) {
                    savedStateHandle[KEY_CONFIRMATION_MESSAGE] = null
                    savedStateHandle[KEY_FIREBASE_ERROR] = ex.localizedMessage
                    Log.e(TAG, "Error changing password.", ex)
                }
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
        viewModelScope.launch {
            try {
                userRepo.deleteUserDataAndAuth()
            } catch (ex: Exception) {
                Log.e(TAG, "Error during account deletion.", ex)
            }
        }
    }

    // --- Problem reporting ---
    fun openBugReport() {
        savedStateHandle[KEY_IS_PROBLEM_REPORT_SHEET_OPEN] = true
    }

    fun closeBugReport() {
        savedStateHandle[KEY_IS_PROBLEM_REPORT_SHEET_OPEN] = false
    }

    fun setProblemReportAlert(message: String?) {
        savedStateHandle[KEY_PROBLEM_REPORT_ALERT_MESSAGE] = message
    }

    fun sendProblemReport(message: String, emptyMsg: String, successMsg: String) {
        if (message.isBlank()) {
            setProblemReportAlert(emptyMsg)
            return
        }

        viewModelScope.launch {
            try {
                userRepo.sendBugReport(message)

                setProblemReportAlert(successMsg)
            } catch (ex: Exception) {
                setProblemReportAlert(ex.localizedMessage ?: "An unknown error occurred")
            }
        }
    }

    // --- UI overlay controls ---
    fun toggleDeleteAlert(show: Boolean) {
        savedStateHandle[KEY_SHOW_DELETE_ACCOUNT_ALERT] = show
    }

    fun toggleLogoutAlert(show: Boolean) {
        savedStateHandle[KEY_SHOW_LOGOUT_ALERT] = show
    }

    // --- Notification and cleanup helpers ---
    fun cancelAllReminders(bookmarks: List<Event>) {
        viewModelScope.launch {
            try {
                reminderScheduler.cancelAllPendingReminders(bookmarks)
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to cancel reminders", ex)
            }
        }
    }

    fun clearErrorMessages() {
        savedStateHandle[KEY_CONFIRMATION_MESSAGE] = null
        savedStateHandle[KEY_FIREBASE_ERROR] = null
        savedStateHandle[KEY_PASSWORD_ERROR] = null
    }

    override fun onCleared() {
        super.onCleared()
        firebaseListener?.remove()
        firebaseListener = null
    }
}