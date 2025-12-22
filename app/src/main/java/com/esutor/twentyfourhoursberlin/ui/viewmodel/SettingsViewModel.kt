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
import com.esutor.twentyfourhoursberlin.data.repository.UserRepository
import com.esutor.twentyfourhoursberlin.notifications.schedule.AndroidReminderScheduler
import com.esutor.twentyfourhoursberlin.utils.checkPassword
import com.esutor.twentyfourhoursberlin.utils.toLanguageOrNull
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        private const val KEY_PASSWORD_ERROR = "passwordError"
        private const val KEY_PROBLEM_REPORT_ALERT_MESSAGE = "problemReportAlertMessage"
        private const val KEY_PUSH_NOTIFICATIONS_ENABLED = "pushNotificationsEnabled"
        private const val KEY_SHOW_DELETE_ACCOUNT_ALERT = "showDeleteAccountAlert"
        private const val KEY_SHOW_LOGOUT_ALERT = "showLogoutAlert"
    }

    // --- STATEFLOWS (UI STATE) ---
    val confirmationMessageResId = savedStateHandle.getStateFlow(KEY_CONFIRMATION_MESSAGE, null as Int?)
    val firebaseError = savedStateHandle.getStateFlow(KEY_FIREBASE_ERROR, null as String?)
    val isProblemReportSheetOpen = savedStateHandle.getStateFlow(KEY_IS_PROBLEM_REPORT_SHEET_OPEN, false)
    val isReauthenticated = savedStateHandle.getStateFlow(KEY_IS_REAUTHENTICATED, false)
    val passwordErrorResId = savedStateHandle.getStateFlow(KEY_PASSWORD_ERROR, null as Int?)
    val problemReportAlertMessage = savedStateHandle.getStateFlow<String?>(KEY_PROBLEM_REPORT_ALERT_MESSAGE, null)
    var pushNotificationsEnabledState = savedStateHandle.getStateFlow(KEY_PUSH_NOTIFICATIONS_ENABLED, true)
    val showDeleteAccountAlert = savedStateHandle.getStateFlow(KEY_SHOW_DELETE_ACCOUNT_ALERT, false)
    val showLogoutAlert = savedStateHandle.getStateFlow(KEY_SHOW_LOGOUT_ALERT, false)

    private val _language = MutableStateFlow<Language?>(null)
    val language: StateFlow<Language?> = _language.asStateFlow()

    // --- LISTENERS & CACHE ---
    private var firebaseListener: ListenerRegistration? = null
    private var currentAppUser: AppUser? = null

    init {
        startUserListener()
    }

    private fun startUserListener() {
        if (firebaseListener == null) {
            firebaseListener = userRepo.addUserListener { user ->
                currentAppUser = user
                savedStateHandle[KEY_PUSH_NOTIFICATIONS_ENABLED] = user?.settings?.pushNotificationsEnabled ?: false
                _language.value = user?.settings?.language?.toLanguageOrNull()
            }
        }
    }

    // --- PROBLEM REPORT ---
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

    // --- ALERTS ---
    fun toggleDeleteAlert(show: Boolean) {
        savedStateHandle[KEY_SHOW_DELETE_ACCOUNT_ALERT] = show
    }

    fun toggleLogoutAlert(show: Boolean) {
        savedStateHandle[KEY_SHOW_LOGOUT_ALERT] = show
    }

    // --- ACTIONS: USER SETTINGS ---
    fun changeLanguage(newLanguage: Language?) {
        val settings = Settings(
            pushNotificationsEnabled = pushNotificationsEnabledState.value,
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

    fun changePushNotifications(enabled: Boolean) {
        val settings = Settings(
            pushNotificationsEnabled = enabled,
            language = _language.value?.label
        )

        viewModelScope.launch {
            try {
                userRepo.updateUserInformation(null, settings)
            } catch (ex: Exception) {
                Log.e(TAG, "Error changing push notifications setting.", ex)
            }
        }
    }

    // --- ACTIONS: ACCOUNT & AUTH ---
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

    // --- HELPERS ---
    fun removeAllPendingNotifications(bookmarks: List<Event>) {
        viewModelScope.launch {
            reminderScheduler.cancelAllPendingReminders(bookmarks)
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