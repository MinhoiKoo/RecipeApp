package com.minhoi.recipeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.model.RecipeDto

class RecipeListViewModel : ViewModel() {

    private var _recipeList = MutableLiveData<List<RecipeDto>>()
    val recipeList : LiveData<List<RecipeDto>>
        get() = _recipeList



    fun getRecipe(ingredients: List<String>) {
        val filterKeyList = arrayListOf<RecipeDto>()
        var remainingIngredients = ingredients.size

        ingredients.forEach { ingredient ->
            Ref.recipeDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val value = data.getValue(RecipeDto::class.java)
                        val key = data.key.toString()
                        if (value != null && value.rcp_PARTS_DTLS?.contains(ingredient) == true) {
                            filterKeyList.add(value)
                            Log.d("filter", key)
                        }
                    }
                    remainingIngredients--
                    if (remainingIngredients == 0) {
                        // All ingredients have been processed, invoke the callback here.
                        _recipeList.value = filterKeyList
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    remainingIngredients--
                    if (remainingIngredients == 0) {
                        // All ingredients have been processed, invoke the callback here.

                    }
                }
            })
        }
    }
}