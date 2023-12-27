package com.minhoi.recipeapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.model.RecipeDataModel
import com.minhoi.recipeapp.model.RecipeRepository
import com.minhoi.recipeapp.model.SearchRecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeListViewModel : ViewModel() {
    private val recipeRepository = RecipeRepository()
    private var _recipeList = MutableLiveData<List<SearchRecipeDto>>()
    val recipeList : LiveData<List<SearchRecipeDto>>
        get() = _recipeList

    suspend fun getRecipeByName(name : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipeData = recipeRepository.getRecipeByName(name)
            withContext(Dispatchers.Main){
                _recipeList.value = recipeData
            }
        }
    }


    fun getRecipe(ingredients: List<String>) {
        val filterKeyList = arrayListOf<SearchRecipeDto>()
        var remainingIngredients = ingredients.size

        ingredients.forEach { ingredient ->
            Ref.recipeDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        val value = data.getValue(SearchRecipeDto::class.java)
                        val key = data.key.toString()
                        if (value != null && value.rcp_PARTS_DTLS?.contains(ingredient) == true) {
                            filterKeyList.add(value)
                            Log.d("filter", key)
                        }
                    }
                    remainingIngredients--
                    if (remainingIngredients == 0) {
                        // All ingredients have been processed, invoke the callback here.
//                        filterKeyList.sortedWith(compareBy<SearchRecipeDto> {
//                            when(it.rcp_PAT2) {
//                                "메인" -> 0
//                                ""
//                            }
//                        })  ㅍ
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