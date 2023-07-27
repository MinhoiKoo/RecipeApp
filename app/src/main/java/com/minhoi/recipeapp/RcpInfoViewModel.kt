package com.minhoi.recipeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import com.minhoi.recipeapp.api.Ref
import com.minhoi.recipeapp.model.KakaoUserRepository
import com.minhoi.recipeapp.model.RecipeDataRepository
import com.minhoi.recipeapp.model.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RcpInfoViewModel : ViewModel() {

    private val kakaoUserRepository = KakaoUserRepository()
    private val recipeDataRepository = RecipeDataRepository()
    private var ref = Ref()
    private var _isBookmarked = MutableLiveData<Boolean>(false)
    val isBookmarked : LiveData<Boolean>
        get() = _isBookmarked


    suspend fun getRecipe(rcpSeq: String) : RecipeDto {
        return recipeDataRepository.getRecipeInfo(rcpSeq)
    }

    suspend fun getUser(): String {
        return withContext(Dispatchers.IO) {
            kakaoUserRepository.getUser()
        }
    }

    fun isBookmark (userId : String, rcpSeq : String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 북마크 하지 않은 레시피이면 DB에 등록 후 색칠된 버튼
                _isBookmarked.value = dataSnapshot.exists()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq!!).addValueEventListener(postListener)
    }

    fun setBookmark(userId : String, rcpSeq: String) {
        Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq).child("whenBookmarked").setValue(ref.getDate())
    }

    fun deleteBookmark(userId : String, rcpSeq: String) {
        Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq).removeValue()
    }

}