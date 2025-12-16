package com.example.a24hberlin.data.repository

import com.example.a24hberlin.BuildConfig
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Settings
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

class UserRepositoryImpl(private val db: FirebaseFirestore) : UserRepository {

    private val auth = Firebase.auth
    private val collRef = db.collection("users")

    private fun FirebaseAuth.getUserDocumentRef(): DocumentReference? {
        val uid = this.currentUser?.uid ?: return null
        return collRef.document(uid)
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

    override fun addUserListener(onChange: (AppUser?) -> Unit): ListenerRegistration? {
        val userRef = auth.getUserDocumentRef()
        return userRef?.let { ref ->
            ref.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error listening for user updates: ${error.message}")
                    onChange(null)
                    return@addSnapshotListener
                }
                val user = snapshot?.toObject<AppUser>()
                onChange(user)
            }
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

    override suspend fun updateUserInformation(favoriteID: String?, settings: Settings?) {
        withContext(Dispatchers.IO) {
            val values = mutableMapOf<String, Any>().apply {
                favoriteID?.let { this["favoriteIDs"] = FieldValue.arrayUnion(it) }
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

    override suspend fun removeFavoriteID(favoriteID: String) {
        withContext(Dispatchers.IO) {
            auth.getUserDocumentRef()
                ?.update("favoriteIDs", FieldValue.arrayRemove(favoriteID))
                ?.await()
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

    override suspend fun login(email: String, password: String) {
        withContext(Dispatchers.IO) {
            auth
                .signInWithEmailAndPassword(email, password)
                .await()
        }
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

    override fun logout() {
        auth.signOut()
    }

    override fun sendBugReport(
        message: String,
        completion: (Exception?) -> Unit
    ) {
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
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(null)
                    println("Bug report sent successfully!")
                } else {
                    completion(task.exception)
                }
            }
    }
}