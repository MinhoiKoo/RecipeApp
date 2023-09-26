package com.minhoi.recipeapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minhoi.recipeapp.api.MyApi
import com.minhoi.recipeapp.api.RetrofitInstance
import com.minhoi.recipeapp.model.KakaoUserRepository
import com.minhoi.recipeapp.model.RecipeDataModel
import com.minhoi.recipeapp.model.RecipeDataRepository
import com.minhoi.recipeapp.model.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val retrofitInstance : MyApi = RetrofitInstance.getInstance().create(MyApi::class.java)
    private val kakaoUserRepository = KakaoUserRepository()
    private val recipeDataRepository = RecipeDataRepository()

    private var _mutableRcpList = MutableLiveData<List<RecipeDataModel>>()
    val liveRcpList : LiveData<List<RecipeDataModel>>
        get() = _mutableRcpList

    private var _mutablePopularList = MutableLiveData<ArrayList<RecipeDataModel>>()
    val livePopularList : LiveData<ArrayList<RecipeDataModel>>
        get() = _mutablePopularList

    private var _mutableIngredientList = MutableLiveData<ArrayList<String>>()
    val liveIngredientList : LiveData<ArrayList<String>> = _mutableIngredientList
    private var tempIngredientList = arrayListOf<String>()

    suspend fun getUser() : String {
        return kakaoUserRepository.getUser()
    }

    init {
        _mutableIngredientList.value = tempIngredientList

        viewModelScope.launch {
            setPopularRecipe()
            setRandomRecipe()
        }
    }

    suspend fun setRandomRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            val randomList = recipeDataRepository.getRandomRcp()
            withContext(Dispatchers.Main) {
                _mutableRcpList.value = randomList
            }
        }
    }

    private suspend fun setPopularRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            val popularList = recipeDataRepository.getPopularRcp()
            withContext(Dispatchers.Main) {
                _mutablePopularList.value = popularList
            }
        }
    }

    fun addIngredient(item : String) {
        tempIngredientList.add(item)
        _mutableIngredientList.value = tempIngredientList
    }

    fun deleteIngredient(position : Int) {
        tempIngredientList.removeAt(position)
        _mutableIngredientList.value = tempIngredientList
    }

    fun isIngredientListEmpty() : Boolean {
        return _mutableIngredientList.value!!.isEmpty()
    }
}