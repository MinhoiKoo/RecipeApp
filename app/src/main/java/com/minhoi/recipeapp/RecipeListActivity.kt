package com.minhoi.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.model.RecipeDto

class RecipeListActivity : AppCompatActivity() {
    private val TAG = RecipeListActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        val intent = intent
        val recipeList = intent.getStringArrayListExtra("recipeList")

        val query = Ref.recipeDataRef.child("RecipeData").orderByKey().startAt(recipeList?.get(0)).endAt(
            recipeList?.get(recipeList.size - 1)
        )

        Ref.recipeDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}