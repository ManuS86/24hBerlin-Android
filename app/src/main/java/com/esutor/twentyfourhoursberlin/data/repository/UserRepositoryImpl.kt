package com.esutor.twentyfourhoursberlin.data.repository

import android.util.Log
import com.esutor.twentyfourhoursberlin.BuildConfig
import com.esutor.twentyfourhoursberlin.data.model.AppUser
import com.esutor.twentyfourhoursberlin.data.model.Settings
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val TAG = "UserRepositoryImpl"

class UserRepositoryImpl(private val db: FirebaseFirestore) : UserRepository {

    private val auth = Firebase.auth
    private val collRef = db.collection("users")

    private fun FirebaseAuth.getUserDocumentRef(): DocumentReference? {
        val uid = this.currentUser?.uid ?: return null
        return collRef.document(uid)
    }

    override suspend fun login(email: String, password: String) {
        withContext(Dispatchers.IO) {
            auth
                .signInWithEmailAndPassword(email, password)
                .await()
        }
    }

    override suspend fun register(email: String, password: String) {
        withContext(Dispatchers.IO) {
            val result = auth.createUserWithEmailAndPassword(email, password).await()

            result.user?.let { user ->
                collRef
                    .document(user.uid)
                    .set(AppUser())
                    .await()
            }
        }
    }

    override fun logout() {
        auth.signOut()
    }

    override suspend fun reAuthenticate(password: String) {
        withContext(Dispatchers.IO) {
            val user = auth.currentUser
                ?: throw IllegalStateException("User must be logged in to re-authenticate.")

            val email = user.email
                ?: throw IllegalStateException("User account is missing an email address.")

            val credential = EmailAuthProvider.getCredential(email, password)

            user
                .reauthenticate(credential)
                .await()
        }
    }

    override suspend fun deleteUserDataAndAuth() {
        withContext(Dispatchers.IO) {
            auth.getUserDocumentRef()
                ?.delete()
                ?.await()

            auth.currentUser
                ?.delete()
                ?.await()
        }
    }

    override suspend fun changeEmail(email: String) {
        withContext(Dispatchers.IO) {
            auth.currentUser
                ?.verifyBeforeUpdateEmail(email)
                ?.await()
        }
    }

    override suspend fun changePassword(password: String) {
        withContext(Dispatchers.IO) {
            auth.currentUser
                ?.updatePassword(password)
                ?.await()
        }
    }

    override suspend fun resetPassword(email: String) {
        withContext(Dispatchers.IO) {
            auth
                .sendPasswordResetEmail(email)
                .await()
        }
    }

    override fun addUserListener(onChange: (AppUser?) -> Unit): ListenerRegistration? {
        val userRef = auth.getUserDocumentRef()
        return userRef?.let { ref ->
            ref.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error listening for user updates: ${error.message}", error)
                    onChange(null)
                    return@addSnapshotListener
                }
                val user = snapshot?.toObject<AppUser>()
                onChange(user)
            }
        }
    }

    override suspend fun updateUserInformation(bookmarkId: String?, settings: Settings?) {
        withContext(Dispatchers.IO) {
            val values = mutableMapOf<String, Any>().apply {
                bookmarkId?.let { this["bookmarkIDs"] = FieldValue.arrayUnion(it) }
                settings?.let {
                    this["settings"] = mapOf(
                        "pushNotificationsEnabled" to it.pushNotificationsEnabled,
                        "language" to it.language
                    )
                }
            }

            if (values.isEmpty()) return@withContext

            auth.getUserDocumentRef()
                ?.update(values)
                ?.await()
        }
    }

    override suspend fun removeBookmarkId(bookmarkId: String) {
        withContext(Dispatchers.IO) {
            auth.getUserDocumentRef()
                ?.update("bookmarkIDs", FieldValue.arrayRemove(bookmarkId))
                ?.await()
        }
    }

    override suspend fun sendBugReport(message: String) {
        withContext(Dispatchers.IO) {
            val bugReportData = mapOf(
                "message" to message,
                "user_uid" to auth.currentUser?.uid,
                "user_email" to auth.currentUser?.email,
                "timestamp" to FieldValue.serverTimestamp(),
                "app_version" to BuildConfig.VERSION_NAME,
                "device_info" to android.os.Build.MODEL
            )

            db.collection("bug_reports")
                .add(bugReportData)
                .await()
        }
    }
}