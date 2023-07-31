package com.minhoi.recipeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minhoi.recipeapp.api.MyApi
import com.minhoi.recipeapp.api.RetrofitInstance
import com.minhoi.recipeapp.model.RcpResponse
import com.minhoi.recipeapp.model.RecipeDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SearchViewModel : ViewModel() {

    private val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)

    private var _mutableSearchList = MutableLiveData<List<RecipeDto>>()
    val searchList : LiveData<List<RecipeDto>>
        get() = _mutableSearchList

    private var originaSearchList : List<RecipeDto>? = null

    var _mutableSearchInput = MutableLiveData<String>()

    val searchInput : LiveData<String>
        get() = _mutableSearchInput


    fun searchRcp() {
        val call = retrofitInstance.searchRecipe(1,1000,_mutableSearchInput.value.toString())
        call.enqueue(object : Callback<RcpResponse> {
            override fun onResponse(call: Call<RcpResponse>, response: Response<RcpResponse>) {
                originaSearchList = response.body()?.COOKRCP01?.row
                _mutableSearchList.value = originaSearchList.orEmpty()
                Log.d("List", response.body()?.COOKRCP01?.row.toString())
            }

            override fun onFailure(call: Call<RcpResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


    fun filter(minKcal: String, maxKcal: String, type: String) {
        // 필터링할 대상을 원본 검색 결과로 설정
        var filteredList = originaSearchList

        filteredList = filterByKcal(filteredList, minKcal, maxKcal)
        filteredList = filterByType(filteredList, type)

        _mutableSearchList.value = filteredList.orEmpty()
    }

    suspend fun getRecipeName(name : String) : ArrayList<String> {

        return suspendCoroutine {
            val call = retrofitInstance.searchRecipe(1,1000, name)
            call.enqueue(object : Callback<RcpResponse> {
                override fun onResponse(call: Call<RcpResponse>, response: Response<RcpResponse>) {

                    Log.d("List", response.body()?.COOKRCP01?.row.toString())
                    val recipeList = response.body()?.COOKRCP01?.row
                    val nameList = arrayListOf<String>()
                    if (recipeList != null) {
                        for(i in recipeList) {
                            nameList.add(i.rcp_NM)
                        }
                    }
                    it.resume(nameList)
                }

                override fun onFailure(call: Call<RcpResponse>, t: Throwable) {
                    t.cause?.let { error -> it.resumeWithException(error) }
                }
            })
        }
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