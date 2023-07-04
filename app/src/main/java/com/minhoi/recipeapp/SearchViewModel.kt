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

class SearchViewModel : ViewModel() {

    private val retrofitInstance = RetrofitInstance.getInstance().create(MyApi::class.java)

    private var _mutableSearchList = MutableLiveData<List<RecipeDto>>()
    val searchList : LiveData<List<RecipeDto>>
        get() = _mutableSearchList

    var _mutableSearchInput = MutableLiveData<String>()

    val searchInput : LiveData<String>
        get() = _mutableSearchInput


    fun searchRcp() {
        val call = retrofitInstance.searchRecipe(1,200,_mutableSearchInput.value.toString())
        call.enqueue(object : Callback<RcpResponse> {
            override fun onResponse(call: Call<RcpResponse>, response: Response<RcpResponse>) {
                _mutableSearchList.value = response.body()?.COOKRCP01?.row
                Log.d("List", response.body()?.COOKRCP01?.row.toString())
            }

            override fun onFailure(call: Call<RcpResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun filterByKcal(minAmount : Double, maxAmount : Double) {
        val filterdList = _mutableSearchList.value?.filter {
            it.INFO_ENG?.toDouble()!! in minAmount..maxAmount
        }
        _mutableSearchList.value = filterdList!!
    }
}