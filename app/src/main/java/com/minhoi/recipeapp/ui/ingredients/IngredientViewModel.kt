package com.minhoi.recipeapp.ui.ingredients

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhoi.recipeapp.adapter.recyclerview.IngredientListItem
import com.minhoi.recipeapp.model.IngredientDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.minhoi.recipeapp.model.IngredientDto
import com.minhoi.recipeapp.model.SelectedIngredientDto

class IngredientViewModel : ViewModel() {

    private val ingredientDataRepository = IngredientDataRepository()
    private var _vegetableList = MutableLiveData<List<IngredientDto>>()
    val vegetableList : LiveData<List<IngredientDto>> = _vegetableList

    private var _meatList = MutableLiveData<List<IngredientDto>>()
    val meatList : LiveData<List<IngredientDto>> = _meatList

    private var _seafoodList = MutableLiveData<List<IngredientDto>>()
        val seafoodList : LiveData<List<IngredientDto>> = _seafoodList

    private var _fruitList = MutableLiveData<List<IngredientDto>>()
        val fruitList : LiveData<List<IngredientDto>> = _fruitList

    private var _ingredientList = MutableLiveData<ArrayList<SelectedIngredientDto>>()
    val ingredientList : LiveData<ArrayList<SelectedIngredientDto>>
        get() = _ingredientList

    private val tempList = arrayListOf<SelectedIngredientDto>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val vegetableList = ingredientDataRepository.getVegetable()
            val meatList = ingredientDataRepository.getMeat()
            val seafoodList = ingredientDataRepository.getSeafood()
            val fruitList = ingredientDataRepository.getFruit()
            withContext(Dispatchers.Main) {
                _vegetableList.value = vegetableList
                _meatList.value = meatList
                _seafoodList.value = seafoodList
                _fruitList.value = fruitList
            }
        }
    }

    fun addIngredient(item : SelectedIngredientDto) {
        when(tempList.contains(item)) {
            true -> deleteIngredient(item)
            else -> tempList.add(item)
        }
        _ingredientList.value = tempList
    }

    fun deleteIngredient(item : SelectedIngredientDto) {
        tempList.remove(item)
        _ingredientList.value = tempList
    }

}