package com.minhoi.recipeapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.minhoi.recipeapp.api.Ref

class MypageViewModel(application: Application) : AndroidViewModel(application) {

    val AndroidViewModel.context: Context
        get() = getApplication<Application>().applicationContext

    val userNickname = MutableLiveData<String>()

    private var _isLogin = MutableLiveData<Boolean>()
    val isLogin : LiveData<Boolean>
        get() = _isLogin

    fun logOut() {
        _isLogin.value = false
    }

    fun nickNameChange(nickName : String) {
        UserApiClient.instance.me { user, error ->
            val userId = user?.id.toString()
            Ref.userRef.child(userId).child("nickName").setValue(nickName)
        }
        userNickname.value = nickName
    }

    fun kakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {

            } else if (token != null) {
                //TODO: 최종적으로 카카오로그인 및 유저정보 가져온 결과

                UserApiClient.instance.me{ user, error ->
                    if (error != null) {
                        // 에러 발생
                    } else if (user != null) {
                        _isLogin.value = true

                        val userId = user.id.toString()
                        val nickname = user.kakaoAccount?.profile?.nickname
                        val email = user.kakaoAccount?.email


                        val postListener = object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // 처음 가입한 사용자는 DB에 사용자 정보 저장, 이미 한번 로그인 한 사용자는 로그인만.
                                if(!dataSnapshot.exists()) {
                                    val user = User(userId, nickname.toString())
                                    Ref.userRef.child(userId).setValue(user)
                                } else {
                                    userNickname.value = nickname.toString()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Getting Post failed, log a message

                            }
                        }
                        Ref.userRef.child(userId).addValueEventListener(postListener)

                    }
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
//                    TextMsg(this, "카카오톡으로 로그인 실패 : ${error}")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    fun isValidToken() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        //로그인 필요
//                        binding.notLoginLayout.visibility = View.VISIBLE
                        _isLogin.value = false
                    }
                    else {
                        //기타 에러
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    _isLogin.value = true
                    UserApiClient.instance.me { user, error ->
                        if (user != null) {
                            Ref.userRef.child(user.id.toString()).child("nickName").get().addOnSuccessListener {
                                userNickname.value = it.value.toString()
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                }
            }
        }
        else {
            //로그인 필요
//            binding.notLoginLayout.visibility = View.VISIBLE
            _isLogin.value = false

        }
    }



}