package com.esutor.twentyfourhoursberlin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.esutor.twentyfourhoursberlin.data.api.EventApi
import com.esutor.twentyfourhoursberlin.data.repository.events.EventRepositoryImpl
import com.esutor.twentyfourhoursberlin.data.repository.user.UserRepositoryImpl
import com.esutor.twentyfourhoursberlin.managers.internetcopnnectionobserver.AndroidConnectivityObserver
import com.esutor.twentyfourhoursberlin.managers.permissionmanager.AndroidPermissionManager
import com.esutor.twentyfourhoursberlin.notifications.reminderscheduler.AndroidReminderScheduler
import com.esutor.twentyfourhoursberlin.ui.viewmodel.AuthViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.ConnectivityViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

object ViewModelFactoryHelper {

    fun provideAuthViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                ?: throw IllegalStateException("Application missing")

            return AuthViewModel(
                savedStateHandle = extras.createSavedStateHandle(),
                userRepo = UserRepositoryImpl(FirebaseFirestore.getInstance()),
                auth = Firebase.auth,
                analytics = Firebase.analytics,
                permissionManager = AndroidPermissionManager(application)
            ) as T
        }
    }

    fun provideConnectivityViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                ?: throw IllegalStateException("Application missing")
            return ConnectivityViewModel(
                AndroidConnectivityObserver(application)
            ) as T
        }
    }

    fun provideEventViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                ?: throw IllegalStateException("Application missing")

            return EventViewModel(
                savedStateHandle = extras.createSavedStateHandle(),
                eventRepo = EventRepositoryImpl(EventApi),
                userRepo = UserRepositoryImpl(FirebaseFirestore.getInstance()),
                permissionManager = AndroidPermissionManager(application),
                reminderScheduler = AndroidReminderScheduler(application),
            ) as T
        }
    }

    fun provideSettingsViewModelFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                ?: throw IllegalStateException("Application missing")

            return SettingsViewModel(
                savedStateHandle = extras.createSavedStateHandle(),
                userRepo = UserRepositoryImpl(FirebaseFirestore.getInstance()),
                reminderScheduler = AndroidReminderScheduler(application),
            ) as T
        }
    }
}