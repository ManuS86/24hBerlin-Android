package com.example.a24hberlin.data.repository

import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Settings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private var db: FirebaseFirestore) {
    private val auth = Firebase.auth
    private val collRef = db.collection("users")
    private val userRef: DocumentReference?

    init {
        db = FirebaseFirestore.getInstance()
        userRef = auth.currentUserRef()
    }

    suspend fun register(email: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()

        result.user?.let {
            collRef
                .document(result.user!!.uid)
                .set(AppUser())
        }?.await()
    }

    fun addUserListener(onChange: (AppUser?) -> Unit): ListenerRegistration? {
        return userRef?.let {
            userRef
            userRef.addSnapshotListener { snapshot, error ->
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

    suspend fun changeEmail(email: String) {
        auth.currentUser
            ?.verifyBeforeUpdateEmail(email)
            ?.await()
    }

    suspend fun changePassword(password: String) {
        auth.currentUser
            ?.updatePassword(password)
            ?.await()
    }

    suspend fun resetPassword(email: String) {
        auth
            .sendPasswordResetEmail(email)
            .await()
    }


    suspend fun updateUserInformation(favoriteID: String? = null, settings: Settings? = null) {
        val values = mutableMapOf<String, Any>().apply {
            favoriteID?.let { this["favoriteIDs"] = FieldValue.arrayUnion(it) }
            settings?.let {
                this["settings"] = mapOf(
                    "pushNotificationsEnabled" to it.pushNotificationsEnabled,
                    "language" to it.language
                )
            }
        }

        if (values.isEmpty()) return

        userRef
            ?.update(values)
            ?.await()
    }

    suspend fun removeFavoriteID(favoriteID: String) {
        userRef
            ?.update("favoriteIDs", FieldValue.arrayRemove(favoriteID))
            ?.await()
    }

    suspend fun deleteUserDataAndAuth() {
        userRef
            ?.delete()
            ?.await()

        auth.currentUser
            ?.delete()
            ?.await()
    }

    suspend fun login(email: String, password: String) {
        auth
            .signInWithEmailAndPassword(email, password)
            .await()
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun reAuthenticate(password: String) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(user?.email!!, password)

        user
            .reauthenticate(credential)
            .await()
    }

    suspend fun sendBugReport(
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
            }.await()
    }

    private fun FirebaseAuth.currentUserRef(): DocumentReference? {
        val uid = this.currentUser?.uid ?: return null
        return collRef.document(uid)
    }
}