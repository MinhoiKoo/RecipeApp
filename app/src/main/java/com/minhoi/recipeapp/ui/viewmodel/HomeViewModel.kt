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

    private var _mutableRcpList = MutableLiveData<List<RecipeDto>>()
    val liveRcpList : LiveData<List<RecipeDto>>
        get() = _mutableRcpList

    private var _mutablePopularList = MutableLiveData<ArrayList<RecipeDataModel>>()
    val livePopularList : LiveData<ArrayList<RecipeDataModel>>
        get() = _mutablePopularList

    suspend fun getUser() : String {
        return kakaoUserRepository.getUser()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val randomList = setRandomRecipe()
            val popularList = setPopularRecipe()
            withContext(Dispatchers.Main) {
                _mutableRcpList.value = randomList
                _mutablePopularList.value = popularList

            }
        }
    }

    private suspend fun setRandomRecipe() : ArrayList<RecipeDto> {
        return recipeDataRepository.getRandomRcp()
    }

    private suspend fun setPopularRecipe() : ArrayList<RecipeDataModel> {
        return recipeDataRepository.getPopularRcp()
    }






}