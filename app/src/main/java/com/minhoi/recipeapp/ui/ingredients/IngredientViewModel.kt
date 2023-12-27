package com.minhoi.recipeapp.ui.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhoi.recipeapp.model.IngredientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.minhoi.recipeapp.model.IngredientDto
import com.minhoi.recipeapp.model.SelectedIngredientDto

class IngredientViewModel : ViewModel() {

    private val ingredientRepository = IngredientRepository()
    private var _vegetableList = MutableLiveData<List<IngredientDto>>()
    val vegetableList : LiveData<List<IngredientDto>> = _vegetableList

    private var _meatList = MutableLiveData<List<IngredientDto>>()
    val meatList : LiveData<List<IngredientDto>> = _meatList

    private var _seafoodList = MutableLiveData<List<IngredientDto>>()
        val seafoodList : LiveData<List<IngredientDto>> = _seafoodList

    private var _fruitList = MutableLiveData<List<IngredientDto>>()
        val fruitList : LiveData<List<IngredientDto>> = _fruitList

    // 선택된 재료와 재료는 Dto 클래스가 다름 (RecyclerView에서 구분하기 위해)
    private var _ingredientList = MutableLiveData<ArrayList<SelectedIngredientDto>>()
    val ingredientList : LiveData<ArrayList<SelectedIngredientDto>>
        get() = _ingredientList

    private val tempList = arrayListOf<SelectedIngredientDto>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val vegetableList = ingredientRepository.getVegetable()
            val meatList = ingredientRepository.getMeat()
            val seafoodList = ingredientRepository.getSeafood()
            val fruitList = ingredientRepository.getFruit()
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