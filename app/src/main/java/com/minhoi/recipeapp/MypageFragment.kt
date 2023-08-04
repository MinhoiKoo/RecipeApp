package com.minhoi.recipeapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false )
        viewModel = ViewModelProvider(this).get(MypageViewModel::class.java)

        // 토큰이 유효한지 검사. 유효하지 않으면 다시 로그인 요구.
        viewModel.isValidToken()

        binding.homeBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_homeFragment)
        }

        binding.searchBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_searchFragment)
        }

        binding.refrigeratorBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_refrigeratorFragment)
        }

        binding.loginBtn.setOnClickListener {
            viewModel.kakaoLogin()
        }

        binding.userBookmarkBtn.setOnClickListener {
            if(viewModel.isLogin.value == true) {
                val intent = Intent(requireActivity(), UserBookmarkListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireActivity(), "로그인 후 이용 가능합니다.", Toast.LENGTH_LONG).show()
            }
        }

        binding.editNickName.setOnClickListener {
            val dialog = NicknameChangeDialog(requireContext())
            dialog.showDialog()

            dialog.setOnClickedListener(object : NicknameChangeDialog.NicknameChangeDialogListener {
                override fun onApplyClicked(nickName: String) {
                    viewModel.nickNameChange(nickName)
                }

            })
        }

        binding.userProfileImage.setOnClickListener {

        }

        viewModel.isLogin.observe(viewLifecycleOwner) {
            when(it) {
                true -> {
                    binding.notLoginLayout.visibility = View.GONE
                    binding.logined.visibility = View.VISIBLE
                }
                else -> {
                    binding.notLoginLayout.visibility = View.VISIBLE
                    binding.logined.visibility = View.GONE
                }
            }
        }


        viewModel.userNickname.observe(viewLifecycleOwner) {
            binding.userNickname.text = it
        }

        binding.logOutBtn.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
//                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
//                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    viewModel.logOut()

                }
            }
        }

        binding.userRecipeAddBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), UserRecipeListActivity::class.java))
        }

        return binding.root
    }

}