package com.minhoi.recipeapp.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeDataRepository {

    suspend fun getRecipeInfo(rcpSeq : String) : RecipeDataModel{

        return suspendCoroutine<RecipeDataModel> { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.getValue(RecipeDataModel::class.java)
                    if(data != null) {
                        continuation.resume(data)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            Ref.myRecipeDataRef.child(rcpSeq).addListenerForSingleValueEvent(postListener)
        }
    }
}