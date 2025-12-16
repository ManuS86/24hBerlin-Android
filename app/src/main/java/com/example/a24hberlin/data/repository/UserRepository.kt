package com.example.a24hberlin.data.repository

import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Settings
import com.google.firebase.firestore.ListenerRegistration

interface UserRepository {
    suspend fun register(email: String, password: String)
    fun addUserListener(onChange: (AppUser?) -> Unit): ListenerRegistration?
    suspend fun changeEmail(email: String)
    suspend fun changePassword(password: String)
    suspend fun resetPassword(email: String)
    suspend fun updateUserInformation(favoriteID: String?, settings: Settings?)
    suspend fun removeFavoriteID(favoriteID: String)
    suspend fun deleteUserDataAndAuth()
    suspend fun login(email: String, password: String)
    suspend fun reAuthenticate(password: String)
    fun logout()
    fun sendBugReport(message: String, completion: (Exception?) -> Unit)
}