package com.minhoi.recipeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.adapter.RecipeListAdapter
import com.minhoi.recipeapp.databinding.FragmentSearchBinding
import com.minhoi.recipeapp.model.RecipeDto

class SearchFragment : Fragment() {

    // 메뉴명, 재료명 으로 검색 가능하도록 구현 예정
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var myAdapter : RecipeListAdapter
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

        binding.homeBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }

        binding.mypageBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_mypageFragment)
        }

        binding.refrigeratorBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_searchFragment_to_refrigeratorFragment)
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

        myAdapter = RecipeListAdapter(requireContext(), {
            val intent = Intent(requireActivity(), RcpInfoActivity::class.java)
            intent.apply {
                putExtra("name", it.rcp_NM)
                putExtra("ingredient", it.rcp_PARTS_DTLS)
                putExtra("manual01", it.manual01)
                putExtra("manual02", it.manual02)
                putExtra("manual03", it.manual03)
                putExtra("image01", it.manual_IMG01)
                putExtra("image02", it.manual_IMG02)
                putExtra("image03", it.manual_IMG03)
                putExtra("imageSrc", it.att_FILE_NO_MK)
                putExtra("rcpSeq", it.rcp_SEQ)
            }
            startActivity(intent)
        })

        viewModel.searchList.observe(viewLifecycleOwner) {
            myAdapter.setLists(it)
        }

        binding.searchRv.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }


//        viewModel.searchList.observe(viewLifecycleOwner) {
//            val adapter = RecipeListAdapter(context!!, it as ArrayList<RecipeDto>)
//            rv.adapter = adapter
//            rv.layoutManager = GridLayoutManager(activity, 1)
//            adapter.setItemClickListener(object : RecipeListAdapter.OnItemClickListener {
//                override fun onClick(v: View, position: Int) {
//                    Log.d("kcal", it[position].info_ENG?.toDouble().toString())
//                }
//
//            })
//        }

        binding.filterBtn.setOnClickListener {

            val dialog = FilterDialog(requireContext())
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

