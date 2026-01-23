package com.esutor.twentyfourhoursberlin.data.repositories.user

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.esutor.twentyfourhoursberlin.BuildConfig
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.models.AppUser
import com.esutor.twentyfourhoursberlin.data.models.Settings
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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

private const val TAG = "UserRepository"

class UserRepositoryImpl(private val db: FirebaseFirestore) : UserRepository {

    private val auth = Firebase.auth
    private val collRef = db.collection("users")

    private suspend fun ensureUserDocumentExists(user: FirebaseUser) {
        val userRef = collRef.document(user.uid)
        val snapshot = userRef.get().await()
        if (!snapshot.exists()) {
            userRef.set(AppUser()).await()
            Log.d(TAG, "New user document initialized for UID: ${user.uid}")
        }
    }

    private fun FirebaseAuth.getUserDocumentRef(): DocumentReference? {
        val uid = this.currentUser?.uid ?: return null
        return collRef.document(uid)
    }

    // --- Auth Entry ---
    override suspend fun login(email: String, password: String) {
        withContext(Dispatchers.IO) {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { ensureUserDocumentExists(it) }
            Unit
        }
    }

    override suspend fun register(email: String, password: String) {
        withContext(Dispatchers.IO) {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { ensureUserDocumentExists(it) }
            Unit
        }
    }

    override suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            val credentialManager = CredentialManager.create(context)

            fun buildReq(filter: Boolean) = GetCredentialRequest.Builder()
                .addCredentialOption(GetGoogleIdOption.Builder()
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setAutoSelectEnabled(true)
                    .setFilterByAuthorizedAccounts(filter)
                    .build())
                .build()

            try {
                val result = try {
                    credentialManager.getCredential(context, buildReq(true))
                } catch (_: NoCredentialException) {
                    credentialManager.getCredential(context, buildReq(false))
                }

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                val user = authResult.user
                if (user != null) {
                    ensureUserDocumentExists(user)
                    Result.success(user)
                } else {
                    Result.failure(Exception("Firebase user is null"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Final Google Sign-In Failure", e)
                Result.failure(e)
            }
        }

    // --- Session Management ---
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

            user.reauthenticate(credential).await()
            Unit
        }
    }

    // --- Account Maintenance ---
    override suspend fun deleteUserDataAndAuth() {
        withContext(Dispatchers.IO) {
            auth.getUserDocumentRef()?.delete()?.await()
            auth.currentUser?.delete()?.await()
            Unit
        }
    }

    override suspend fun changeEmail(email: String) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.verifyBeforeUpdateEmail(email)?.await()
            Unit
        }
    }

    override suspend fun changePassword(password: String) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.updatePassword(password)?.await()
            Unit
        }
    }

    override suspend fun resetPassword(email: String) {
        withContext(Dispatchers.IO) {
            auth.sendPasswordResetEmail(email).await()
            Unit
        }
    }

    // --- Reactive Data ---
    override fun getUserFlow(): Flow<AppUser?> = callbackFlow {
        val uid = auth.currentUser?.uid ?: run {
            trySend(null); close(); return@callbackFlow
        }

        val userRef = collRef.document(uid)
        val registration = userRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Snapshot Listener error", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                trySend(snapshot.toObject<AppUser>())
            } else {
                trySend(AppUser())
            }
        }
        awaitClose { registration.remove() }
    }

    // --- User Data/Settings ---
    override suspend fun updateUserInformation(bookmarkId: String?, settings: Settings?) {
        withContext(Dispatchers.IO) {
            val userRef = auth.getUserDocumentRef() ?: return@withContext
            val values = mutableMapOf<String, Any?>()

            bookmarkId?.takeIf { it.isNotBlank() }?.let {
                values["bookmarkIDs"] = FieldValue.arrayUnion(it)
            }

            settings?.let {
                values["settings.notificationsEnabled"] = it.notificationsEnabled
                values["settings.language"] = it.language
            }

            if (values.isNotEmpty()) {
                userRef.update(values).await()
            }
            Unit
        }
    }

    override suspend fun removeBookmarkIds(bookmarkIds: List<String>) =
        withContext(Dispatchers.IO) {
        if (bookmarkIds.isEmpty()) return@withContext
        val userRef = auth.getUserDocumentRef()
        userRef?.update("bookmarkIDs", FieldValue.arrayRemove(*bookmarkIds.toTypedArray()))?.await()
        Unit
    }

    // --- Support ---
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

            db.collection("bug_reports").add(bugReportData).await()
            Unit
        }
    }
}