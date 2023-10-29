package com.serapbekar.fprojectsb.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.serapbekar.fprojectsb.common.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository  @Inject constructor (private val firebaseAuth: FirebaseAuth) : User {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun checkUserLogin(): Boolean = firebaseAuth.currentUser != null

    fun getFireUserUid(): String = firebaseAuth.currentUser?.uid.orEmpty()

    suspend fun signInUser(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun signUpUser(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().build())?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}

interface User {
    val currentUser: FirebaseUser?
}