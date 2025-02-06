package com.example.a24hberlin.data.repository

import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.data.model.Settings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository(private var db: FirebaseFirestore) {
    private val auth = Firebase.auth
    private val collRef = db.collection("users")
    private val userRef: DocumentReference?

    init {
        db = FirebaseFirestore.getInstance()
        userRef = auth.currentUserRef()
    }

    suspend fun register(email : String, password: String){
        val result = auth.createUserWithEmailAndPassword(email,password).await()

        result.user?.let { user ->
            collRef
                .document(user.uid)
                .set(AppUser(user.uid))
        }
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

    fun changeEmail(email: String) {
        auth.currentUser?.verifyBeforeUpdateEmail(email)
    }

    fun changePassword(password: String) {
        auth.currentUser?.updatePassword(password)
    }

    fun updateUserInformation(favoriteID: String? = null, settings: Settings? = null) {
        val values = mutableMapOf<String, Any>()
        favoriteID?.let { values["favoriteIDs"] = FieldValue.arrayUnion(listOf(it)) }
        settings?.let {
            values["settings"] = mapOf(
                "pushNotificationsEnabled" to it.pushNotificationsEnabled,
                "language" to it.language
            )
        }
        if (values.isEmpty()) return
        userRef?.update(values)
    }

    suspend fun removeFavoriteID(favoriteID: String) {
        userRef?.set(
            mapOf("favoriteIDs" to FieldValue.arrayRemove(listOf(favoriteID)))
        )?.await()
    }

    suspend fun deleteUserDataAndAuth() {
        userRef?.delete()?.await()
        auth.currentUser?.delete()?.await()
    }

    suspend fun login(email : String, password: String){
        auth.signInWithEmailAndPassword(email,password).await()
    }

    fun logout(){
        auth.signOut()
    }

    fun sendBugReport(
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

    private fun FirebaseAuth.currentUserRef(): DocumentReference? {
        val uid = this.currentUser?.uid ?: return null
        return collRef.document(uid)
    }
}