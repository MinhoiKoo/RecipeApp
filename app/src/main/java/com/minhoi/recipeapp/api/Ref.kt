package com.minhoi.recipeapp.api

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class Ref {

    companion object {
        val database = Firebase.database
        val recipeDataRef = database.getReference("RecipeData")
        val userRef = database.getReference("User")
    }

    fun getDate() : String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA).format(currentDateTime)

        return dateFormat
    }
}