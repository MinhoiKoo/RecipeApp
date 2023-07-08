package com.minhoi.recipeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MypageViewModel : ViewModel() {
    val userNickname = MutableLiveData<String>()

    private var _isLogin = MutableLiveData<Boolean>()
    val isLogin : LiveData<Boolean>
        get() = _isLogin


    fun needLogin() {
        _isLogin.value = false
    }

    fun needNotLogin() {
        _isLogin.value = true
    }


}