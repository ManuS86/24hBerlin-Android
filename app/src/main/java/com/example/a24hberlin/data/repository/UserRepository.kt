package com.example.a24hberlin.data.repository

import com.example.a24hberlin.data.model.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class UserRepository(private var db: FirebaseFirestore) {
    private val auth = Firebase.auth
    private val collRef = db.collection("users")
    private val userRef: DocumentReference?

    init {
        db = FirebaseFirestore.getInstance()
        userRef = auth.currentUserRef()
    }

    fun addUserListener(onChange: (AppUser?) -> Unit): ListenerRegistration? {
        return userRef?.let { userRef
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

    private fun FirebaseAuth.currentUserRef(): DocumentReference? {
        val uid = this.currentUser?.uid ?: return null
        return collRef.document(uid)
    }
}