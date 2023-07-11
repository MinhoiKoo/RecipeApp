package com.minhoi.recipeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.minhoi.recipeapp.api.MyApi
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.api.RetrofitInstance
import com.minhoi.recipeapp.model.RecipeDto

class HomeViewModel : ViewModel() {
    private val retrofitInstance : MyApi = RetrofitInstance.getInstance().create(MyApi::class.java)

    private var _mutableRcpList = MutableLiveData<List<RecipeDto>>()
    val liveRcpList : LiveData<List<RecipeDto>>
        get() = _mutableRcpList



    fun getRandomRcp() {

        var count = 0
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val recipeList = mutableListOf<RecipeDto>()
                for (postSnapshot in dataSnapshot.children) {

                    if(count ==6) {
                        break
                    }

                    count++
                    val recipeData = postSnapshot.getValue(RecipeDto::class.java)
                    Log.d("recipeData",recipeData.toString())

                    if (recipeData != null) {
                        recipeList.add(recipeData)
                    }

//                    val recipeData = postSnapshot.getValue(RecipeDto::class.java)
//                    // 게시글의 정보와 게시글의 Key값 DB에 삽입
//                    if (recipeData != null) {
//                        recipeList.add(recipeData)
//                        Log.d("recipelist",recipeData.RCP_NM.toString())
//                        count++
//                    }
                }
                _mutableRcpList.value = recipeList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        Ref.recipeDataRef.addListenerForSingleValueEvent(postListener)

    }






//    fun getRandomRcp(startIdx : Int, endIdx : Int) {
//        val call = retrofitInstance.getRecipe(startIdx, endIdx)
//        call.enqueue(object : Callback<RcpResponse> {
//            override fun onResponse(call: Call<RcpResponse>, response: Response<RcpResponse>) {
//                _mutableRcpList.value = response.body()?.COOKRCP01?.row
//                Log.d("get", response.body()?.COOKRCP01?.row.toString())
//            }
//
//            override fun onFailure(call: Call<RcpResponse>, t: Throwable) {
//                //서버와 연결 실패. 다시 접속해주세요
//            }
//        })
//    }
}