package com.example.a24hberlin.data.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository(private var db: FirebaseFirestore) {
    private val collRef = db.collection("users")

    init {
        db = FirebaseFirestore.getInstance()
    }
}