package com.minhoi.recipeapp.model

import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KakaoUserAPIModel {

    suspend fun getUser() : String {
        return suspendCoroutine {
            UserApiClient.instance.me { user, error ->
                if(user != null) {
                    it.resume(user.id.toString())
                } else {
                    it.resume("")
                }
            }
        }
    }


}