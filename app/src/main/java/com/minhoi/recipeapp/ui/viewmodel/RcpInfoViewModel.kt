package com.minhoi.recipeapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.model.KakaoUserRepository
import com.minhoi.recipeapp.model.RecipeDataModel
import com.minhoi.recipeapp.model.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RcpInfoViewModel : ViewModel() {

    private val TAG = RcpInfoViewModel::class.simpleName
    private val kakaoUserRepository = KakaoUserRepository()
    private val recipeRepository = RecipeRepository()
    private var ref = Ref()
    private var _isBookmarked = MutableLiveData<Boolean>(false)
    val isBookmarked : LiveData<Boolean>
        get() = _isBookmarked


    suspend fun getRecipe(rcpSeq: String) : RecipeDataModel {
        return recipeRepository.getRecipeInfo(rcpSeq)
    }

    suspend fun getUser(): String {
        return withContext(Dispatchers.IO) {
            kakaoUserRepository.getUser()
        }
    }

    // 사용자가 레시피 정보 액티비티에 들어올 때 북마크 한 레시피인지 아닌지 검사
    fun isBookmark (userId : String, rcpSeq : String) {

        when(userId) {
            "" -> _isBookmarked.value = false
            else -> {
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        _isBookmarked.value = dataSnapshot.exists()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                    }
                }
                Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq!!).addValueEventListener(postListener)
            }
        }
    }

    fun setBookmark(userId : String, rcpSeq: String) {
        Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq).child("whenBookmarked").setValue(ref.getDate())
        updateBookmarkCount(rcpSeq, true)
        Log.d(TAG, "setBookmark: $rcpSeq")
    }

    fun deleteBookmark(userId : String, rcpSeq: String) {
        Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq).removeValue()
        updateBookmarkCount(rcpSeq, false)
        Log.d(TAG, "deleteBookmark: $rcpSeq")

    }

    // 사용자가 북마크 추가/제거에 따라 레시피의 bookmarkCount +-1
    private fun updateBookmarkCount(rcpSeq : String, upDown : Boolean) {
        val ref = Ref.myRecipeDataRef
        ref.child(rcpSeq).child("bookmarkCount")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentCount = snapshot.getValue(Int::class.java)?:0
                    when(upDown) {
                        true -> ref.child(rcpSeq).child("bookmarkCount").setValue(currentCount + 1)
                        false -> ref.child(rcpSeq).child("bookmarkCount").setValue(currentCount - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}