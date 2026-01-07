package com.esutor.twentyfourhoursberlin.data.repository.user

import android.content.Context
import com.esutor.twentyfourhoursberlin.data.model.AppUser
import com.esutor.twentyfourhoursberlin.data.model.Settings
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun signInWithGoogle(context: Context): Result<FirebaseUser>
    fun logout()
    suspend fun reAuthenticate(password: String)
    suspend fun deleteUserDataAndAuth()
    suspend fun changeEmail(email: String)
    suspend fun changePassword(password: String)
    suspend fun resetPassword(email: String)
    fun getUserFlow(): Flow<AppUser?>
    suspend fun updateUserInformation(bookmarkId: String?, settings: Settings?)
    suspend fun removeBookmarkIds(bookmarkIds: List<String>)
    suspend fun sendBugReport(message: String)
}