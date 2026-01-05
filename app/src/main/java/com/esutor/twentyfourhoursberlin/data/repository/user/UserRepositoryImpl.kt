package com.esutor.twentyfourhoursberlin.data.repository.user

import android.os.Build
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
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

    override fun getUserFlow(): Flow<AppUser?> = callbackFlow {
        val userRef = auth.getUserDocumentRef() ?: run {
            trySend(null); close(); return@callbackFlow
        }

        val registration = userRef.addSnapshotListener { snapshot, error ->
            if (error == null) trySend(snapshot?.toObject<AppUser>())
        }

        awaitClose { registration.remove() }
    }

    override suspend fun updateUserInformation(bookmarkId: String?, settings: Settings?) {
        withContext(Dispatchers.IO) {
            val values = mutableMapOf<String, Any>().apply {
                bookmarkId?.let { this["bookmarkIDs"] = FieldValue.arrayUnion(it) }
                settings?.let {
                    this["settings"] = mapOf(
                        "notificationsEnabled" to it.notificationsEnabled,
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

    override suspend fun removeBookmarkIds(bookmarkIds: List<String>) {
        if (bookmarkIds.isEmpty()) return
        withContext(Dispatchers.IO) {
            val userRef = auth.getUserDocumentRef()
            userRef?.update("bookmarkIDs", FieldValue.arrayRemove(*bookmarkIds.toTypedArray()))
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
                "device_info" to Build.MODEL
            )

            db.collection("bug_reports")
                .add(bugReportData)
                .await()
        }
    }
}