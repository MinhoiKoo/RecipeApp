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

class HomeViewModel : ViewModel() {
    private val retrofitInstance : MyApi = RetrofitInstance.getInstance().create(MyApi::class.java)

    private var _mutableRcpList = MutableLiveData<List<RecipeDto>>()
    val liveRcpList : LiveData<List<RecipeDto>>
        get() = _mutableRcpList


    fun getRandomRcp(startIdx : Int, endIdx : Int) {
        val call = retrofitInstance.getRecipe(startIdx, endIdx)
        call.enqueue(object : Callback<RcpResponse> {
            override fun onResponse(call: Call<RcpResponse>, response: Response<RcpResponse>) {
                _mutableRcpList.value = response.body()?.COOKRCP01?.row
                Log.d("get", response.body()?.COOKRCP01?.row.toString())
            }

            override fun onFailure(call: Call<RcpResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}