package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.repository.UserRepository
import com.example.a24hberlin.utils.toLanguageOrNull
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val TAG = "SettingsViewModel"
    private val userRepo = UserRepository(db)

    private var _currentAppUser = MutableLiveData<AppUser?>()
    val currentAppUser: LiveData<AppUser?>
        get() = _currentAppUser

    private var _pushNotificationsEnabled = MutableLiveData<Boolean>()
    val pushNotificationsEnabled: LiveData<Boolean>
        get() = _pushNotificationsEnabled

    private var _language = MutableLiveData<Language?>()
    val language: LiveData<Language?>
        get() = _language

    init {
        if (listener == null) {
            listener = userRepo.addUserListener { user ->
                _currentAppUser.value = user
                _pushNotificationsEnabled.value = user?.settings?.pushNotificationsEnabled
                _language.value = user?.settings?.language?.toLanguageOrNull()
            }
        }
    }
}