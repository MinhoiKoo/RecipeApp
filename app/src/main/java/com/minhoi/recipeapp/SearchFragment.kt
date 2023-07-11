package com.minhoi.recipeapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.minhoi.recipeapp.databinding.FragmentSearchBinding
import com.minhoi.recipeapp.model.RecipeDto

class SearchFragment : Fragment() {

    // 메뉴명, 재료명 으로 검색 가능하도록 구현 예정
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        val rv = binding.searchRv

        binding.homeBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }

        binding.mypageBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_mypageFragment)
        }

        binding.inputRecipe.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 수정 필요. EditText <-> liveData 오류로 인한 임시 코드
                viewModel._mutableSearchInput.value = binding.inputRecipe.text.toString()
                viewModel.searchRcp()
                handled = true
                Log.d("edit", viewModel._mutableSearchInput.value.toString())
            }
            handled
        }

        viewModel.searchList.observe(viewLifecycleOwner) {
            val adapter = RcpListAdapter(context!!, it as ArrayList<RecipeDto>)
            rv.adapter = adapter
            rv.layoutManager = GridLayoutManager(activity, 1)
            adapter.setItemClickListener(object : RcpListAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    Log.d("kcal", it[position].info_ENG?.toDouble().toString())
                }

            })
        }

        binding.filterBtn.setOnClickListener {

            val dialog = FilterDialog(context!!)
            dialog.showDialog()

            dialog.setOnClickedListener(object : FilterDialog.FilterDialogListener {
                override fun onApplyClicked(minRange: String, maxRange: String, foodType: String) {
                    viewModel.filter(minRange, maxRange, foodType)
                }
            })
        }

        return binding.root
    }
}

