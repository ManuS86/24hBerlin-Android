package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val TAG = "SettingsViewModel"
    private val userRepo = UserRepository(db)

    private var _confirmationMessage = MutableLiveData<String?>()
    val confirmationMessage: LiveData<String?>
        get() = _confirmationMessage

    private var _currentAppUser = MutableLiveData<AppUser?>()
    val currentAppUser: LiveData<AppUser?>
        get() = _currentAppUser

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var _language = MutableLiveData<Language?>()
    val language: LiveData<Language?>
        get() = _language

    private var _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?>
        get() = _passwordError

    private var _pushNotificationsEnabled = MutableLiveData(false)
    val pushNotificationsEnabled: LiveData<Boolean>
        get() = _pushNotificationsEnabled

    init {
        if (listener == null) {
            listener = userRepo.addUserListener { user ->
                _currentAppUser.value = user
                _pushNotificationsEnabled.value = user?.settings?.pushNotificationsEnabled
                _language.value = user?.settings?.language?.toLanguageOrNull()
            }
        }
    }
    fun changeEmail(email: String) {
        viewModelScope.launch {
            try {
                userRepo.changeEmail(email)
                _errorMessage.value = null
                _confirmationMessage.value = "email_changed_successfully."
            } catch (ex: Exception) {
                _confirmationMessage.value = null
                _errorMessage.value = ex.localizedMessage
                Log.e("Change Email", ex.toString())
            }
        }
    }

    fun changePassword(password: String, confirmPassword: String) {
        _errorMessage.value = null
        _passwordError.value = null

        _passwordError.value = checkPassword(password, confirmPassword)

        if (errorMessage == null && passwordError == null) {
            viewModelScope.launch {
                try {
                    userRepo.changePassword(password)
                    _errorMessage.value = null
                    _passwordError.value = null
                    _confirmationMessage.value = "password_changed_successfully."
                } catch (ex: Exception) {
                    _confirmationMessage.value = null
                        _errorMessage.value = ex.localizedMessage
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

    fun reauthenticate() {
        _errorMessage.value = null
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

    fun saveSettings() {
        val settings = Settings(
            pushNotificationsEnabled = _pushNotificationsEnabled.value ?: false,
            language = if (_language != null) {
                _language.value?.label
            } else {
                null
            }
        )

        try {
            userRepo.updateUserInformation(settings = settings)
        } catch (ex: Exception) {
            Log.e("Save Settings", ex.toString())
        }
    }

    fun sendBugReport(message: String, completion: (Exception?) -> Unit) {
        userRepo.sendBugReport(message, completion)
    }

    override fun onCleared() {
        super.onCleared()
        listener?.remove()
        listener = null
    }
}