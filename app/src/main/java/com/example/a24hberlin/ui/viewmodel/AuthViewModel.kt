package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.repository.UserRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private var analytics: FirebaseAnalytics
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val TAG = "AuthViewModel"
    private val userRepo = UserRepository(db)

    private var _currentUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private var _currentAppUser = MutableLiveData<AppUser?>()
    val currentAppUser: LiveData<AppUser?>
        get() = _currentAppUser

    init {
        if (auth.currentUser != null) {
            setUpUserEnv()
        }
        if (listener == null) {
                listener = userRepo.addUserListener { user ->
                    _currentAppUser.value = user
            }
        }
        analytics = Firebase.analytics
    }

    private fun setUpUserEnv() {
        _currentUser.value = auth.currentUser
    }
}