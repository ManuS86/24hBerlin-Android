package com.example.a24hberlin.data.repository

import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Settings
import com.google.firebase.firestore.ListenerRegistration

interface UserRepository {
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    fun logout()
    suspend fun reAuthenticate(password: String)
    suspend fun deleteUserDataAndAuth()
    suspend fun changeEmail(email: String)
    suspend fun changePassword(password: String)
    suspend fun resetPassword(email: String)
    fun addUserListener(onChange: (AppUser?) -> Unit): ListenerRegistration?
    suspend fun updateUserInformation(favoriteID: String?, settings: Settings?)
    suspend fun removeFavoriteID(favoriteID: String)
    suspend fun sendBugReport(message: String)
}