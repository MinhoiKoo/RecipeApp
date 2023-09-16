package com.minhoi.recipeapp.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecipeDataRepository {

    suspend fun getRecipeInfo(rcpSeq : String) : RecipeDataModel{

        return suspendCoroutine { continuation ->
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

    suspend fun getRandomRcp() : ArrayList<RecipeDto> {

        return suspendCoroutine {
            var count = 0
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val recipeList = arrayListOf<RecipeDto>()
                    for (postSnapshot in dataSnapshot.children) {
                        if(count ==6) {break}
                        val recipeData = postSnapshot.getValue(RecipeDto::class.java)
                        Log.d("recipeData",recipeData.toString())

                        if (recipeData != null) {
                            recipeList.add(recipeData)
                            count++
                        }
                    }
                    it.resume(recipeList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            Ref.recipeDataRef.addListenerForSingleValueEvent(postListener)
        }


    }

    suspend fun getPopularRcp() : ArrayList<RecipeDataModel> {
        return suspendCoroutine {
            val queue = arrayListOf<RecipeDataModel>()
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children) {
                        val recipeData = data.getValue(RecipeDataModel::class.java)
                        if(recipeData != null) {
                            queue.add(recipeData)
                        }
                    }
                    it.resume(queue)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
            Ref.myRecipeDataRef.orderByChild("bookmarkCount").addListenerForSingleValueEvent(postListener)
        }
    }
}