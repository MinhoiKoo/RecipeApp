package com.minhoi.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.minhoi.recipeapp.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private lateinit var binding : FragmentMypageBinding
    private lateinit var viewModel: MypageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        // 토큰이 유효한지 검사. 유효하지 않으면 다시 로그인 요구.
        isValidToken()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false )
        viewModel = ViewModelProvider(this).get(MypageViewModel::class.java)

        binding.homeBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_homeFragment)
        }

        binding.searchBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_searchFragment)
        }

        binding.loginBtn.setOnClickListener {
            kakaoLogin()
        }

        viewModel.isLogin.observe(viewLifecycleOwner) {
            if(it) {
                binding.notLoginLayout.visibility = View.GONE
                binding.logined.visibility = View.VISIBLE
            }
            else {
                binding.notLoginLayout.visibility = View.VISIBLE
                binding.logined.visibility = View.GONE
            }
        }

        binding.logOutBtn.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
//                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
//                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    viewModel.needLogin()

                }
            }
        }

        return binding.root
    }




    private fun kakaoLogin() {
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
                        viewModel.needNotLogin()

                        val userId = user.id.toString()
                        val nickname = user.kakaoAccount?.profile?.nickname
                        val email = user.kakaoAccount?.email
                        Log.d("user", "id :$userId\nnickname : $nickname\nemail : $email")
                        Log.d("hasSignUp", user.hasSignedUp.toString())
                    }
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
//                    TextMsg(this, "카카오톡으로 로그인 실패 : ${error}")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }


    private fun isValidToken() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        //로그인 필요
//                        binding.notLoginLayout.visibility = View.VISIBLE
                        viewModel.needLogin()
                    }
                    else {
                        //기타 에러
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    viewModel.needNotLogin()

                }
            }
        }
        else {
            //로그인 필요
//            binding.notLoginLayout.visibility = View.VISIBLE
            viewModel.needLogin()

        }
    }

}