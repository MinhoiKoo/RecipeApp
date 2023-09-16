package com.minhoi.recipeapp.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IngredientDataRepository {

    suspend fun getVegetable() : ArrayList<IngredientDto> {
        val vegetableList = arrayListOf<IngredientDto>()
        return suspendCoroutine { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(data in dataSnapshot.children) {
                        val item = data.getValue(IngredientDto::class.java)
                        if(item != null) {
                            vegetableList.add(item)
                        }
                    }
                    continuation.resume(vegetableList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            Ref.ingredientDataRef.child("vegetables").addListenerForSingleValueEvent(postListener)
        }
    }

    suspend fun getMeat() : ArrayList<IngredientDto> {
        val meatList = arrayListOf<IngredientDto>()
        return suspendCoroutine { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(data in dataSnapshot.children) {
                        val item = data.getValue(IngredientDto::class.java)
                        if(item != null) {
                            meatList.add(item)
                        }
                    }
                    continuation.resume(meatList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            Ref.ingredientDataRef.child("meat").addListenerForSingleValueEvent(postListener)
        }
    }

    suspend fun getSeafood() : ArrayList<IngredientDto> {
        val seafoodList = arrayListOf<IngredientDto>()
        return suspendCoroutine { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(data in dataSnapshot.children) {
                        val item = data.getValue(IngredientDto::class.java)
                        if(item != null) {
                            seafoodList.add(item)
                        }
                    }
                    continuation.resume(seafoodList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            Ref.ingredientDataRef.child("seafoods").addListenerForSingleValueEvent(postListener)
        }
    }

    suspend fun getFruit() : ArrayList<IngredientDto> {
        val fruitList = arrayListOf<IngredientDto>()
        return suspendCoroutine { continuation ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(data in dataSnapshot.children) {
                        val item = data.getValue(IngredientDto::class.java)
                        if(item != null) {
                            fruitList.add(item)
                        }
                    }
                    continuation.resume(fruitList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    continuation.resumeWithException(databaseError.toException())
                }
            }
            Ref.ingredientDataRef.child("fruits").addListenerForSingleValueEvent(postListener)
        }
    }
}