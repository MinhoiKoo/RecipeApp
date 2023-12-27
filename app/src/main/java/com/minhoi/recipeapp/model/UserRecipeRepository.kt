package com.minhoi.recipeapp.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRecipeRepository {

    suspend fun getUserRecipeByKey(uid : String, key : String) : UserRecipeData {
        return suspendCoroutine { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.getValue(UserRecipeData::class.java)
                    data?.let { recipe ->
                        continuation.resume(data)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            Ref.userRecipeDataRef.child(uid).child(key).addListenerForSingleValueEvent(postListener)
        }
    }

    fun editUserRecipe(uid : String, key : String, data : UserRecipeData) {
        Ref.userRecipeDataRef.child(uid).child(key).setValue(data)
    }
}