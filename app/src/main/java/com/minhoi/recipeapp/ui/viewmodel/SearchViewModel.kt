package com.minhoi.recipeapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minhoi.recipeapp.api.MyApi
import com.minhoi.recipeapp.api.RetrofitInstance
import com.minhoi.recipeapp.model.RecipeRepository
import com.minhoi.recipeapp.model.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class SearchViewModel : ViewModel() {

    private val recipeRepository = RecipeRepository()
    private val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)

    private var _mutableSearchList = MutableLiveData<List<RecipeDto>>()
    val searchList : LiveData<List<RecipeDto>>
        get() = _mutableSearchList

    private var originaSearchList : List<RecipeDto>? = null

    private var _mutableAutoCompleteList= MutableLiveData<ArrayList<Pair<String, String>>>()
    val autoCompleteList : LiveData<ArrayList<Pair<String,String>>> = _mutableAutoCompleteList

    var _mutableSearchInput = MutableLiveData<String>()

    val searchInput : LiveData<String>
        get() = _mutableSearchInput


    suspend fun searchRcp() {
//        val response = retrofitInstance.searchRecipe(1,1000,_mutableSearchInput.value.toString())
//        withContext(Dispatchers.Main) {
//            if(response.isSuccessful) {
//                originaSearchList = response.body()?.COOKRCP01?.row
//                _mutableSearchList.value = originaSearchList.orEmpty()
//                Log.d("List", response.body()?.COOKRCP01?.row.toString())
//            } else { }
//        }

        recipeRepository.getRecipeByName(_mutableSearchInput.value.toString())


    }

    suspend fun getRecipeName(name : String) {

        val searchRange = listOf(Pair(1,1000), Pair(1001, 2000), Pair(2001,3000))
        val keyNameList = arrayListOf<Pair<String, String>>()
        repeat(3) {
            val response = retrofitInstance.searchRecipe(searchRange[it].first, searchRange[it].second, name)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("List", response.body()?.COOKRCP01?.row.toString())
                    val recipeList = response.body()?.COOKRCP01?.row
                    if (recipeList != null) {
                        for (data in recipeList) {
                            keyNameList.add(Pair(data.rcp_SEQ, data.rcp_NM))
                        }
                        _mutableAutoCompleteList.value = keyNameList
                    }
                }
            }
        }

    }

    fun filter(minKcal: String, maxKcal: String, type: String) {
        // 필터링할 대상을 원본 검색 결과로 설정
        var filteredList = originaSearchList

        filteredList = filterByKcal(filteredList, minKcal, maxKcal)
        filteredList = filterByType(filteredList, type)

        _mutableSearchList.value = filteredList.orEmpty()
    }

    private fun filterByKcal(list: List<RecipeDto>?, minAmount: String, maxAmount: String): List<RecipeDto>? {
        if (minAmount.isNotEmpty() && maxAmount.isNotEmpty()) {
            return list?.filter {
                it.info_ENG?.toDoubleOrNull()!! in minAmount.toDouble()..maxAmount.toDouble()
            }
        }
        return list
    }

    private fun filterByType(list: List<RecipeDto>?, type: String): List<RecipeDto>? {
        if (type.isNotEmpty()) {
            return list?.filter {
                it.rcp_PAT2 == type
            }
        }
        return list
    }
}