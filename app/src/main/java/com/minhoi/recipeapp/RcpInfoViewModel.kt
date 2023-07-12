package com.minhoi.recipeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhoi.recipeapp.api.Ref

class RcpInfoViewModel : ViewModel() {

    private var _isBookmarked = MutableLiveData<Boolean>()
    val isBookmarked : LiveData<Boolean>
        get() = _isBookmarked


    fun isBookmark (userId : String, rcpSeq : String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 북마크 하지 않은 레시피이면 DB에 등록 후 색칠된 버튼
                if(!dataSnapshot.exists()) {
//                    Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq).setValue("")
                    _isBookmarked.value = false
                }
                // 이미 북마크 되어있는 레시피이면 DB에서 삭제. 색칠되지 않은 버튼
                else {
//                    Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq).removeValue()
                    _isBookmarked.value = true
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq!!).addValueEventListener(postListener)
    }

    fun setBookmark(userId : String, rcpSeq: String) {
        Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq).setValue("")
    }

    fun deleteBookmark(userId : String, rcpSeq: String) {
        Ref.userRef.child(userId).child("bookmarkedRecipe").child(rcpSeq).removeValue()
    }

}