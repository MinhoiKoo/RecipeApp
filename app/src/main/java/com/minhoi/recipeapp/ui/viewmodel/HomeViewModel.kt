package com.minhoi.recipeapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.MyApi
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.api.RetrofitInstance
import com.minhoi.recipeapp.model.KakaoUserRepository
import com.minhoi.recipeapp.model.RecipeDataModel
import com.minhoi.recipeapp.model.RecipeDto
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HomeViewModel : ViewModel() {
    private val retrofitInstance : MyApi = RetrofitInstance.getInstance().create(MyApi::class.java)
    private val kakaoUserRepository = KakaoUserRepository()

    private var _mutableRcpList = MutableLiveData<List<RecipeDto>>()
    val liveRcpList : LiveData<List<RecipeDto>>
        get() = _mutableRcpList

    suspend fun getUser() : String {
        return kakaoUserRepository.getUser()
    }

    init {
        getRandomRcp()
    }

    fun getRandomRcp() {

        var count = 0
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val recipeList = mutableListOf<RecipeDto>()
                for (postSnapshot in dataSnapshot.children) {
                    if(count ==6) {break}
                    val recipeData = postSnapshot.getValue(RecipeDto::class.java)
                    Log.d("recipeData",recipeData.toString())

                    if (recipeData != null) {
                        recipeList.add(recipeData)
                        count++
                    }
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

    // bookmarkCount 순으로 오름차순 정렬하여 deque에 삽입 후 반환
    suspend fun getPopularRcp() : ArrayList<RecipeDataModel> {
        return suspendCoroutine {
            val queue = arrayListOf<RecipeDataModel>()
            val postListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children) {
                        val recipeData = data.getValue(RecipeDataModel::class.java)
                        if(recipeData != null) {
                            queue.add(recipeData)
                        }
                    }
                    it.resume(queue)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
            Ref.myRecipeDataRef.orderByChild("bookmarkCount").addListenerForSingleValueEvent(postListener)
        }
    }


}