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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhoi.recipeapp.adapter.RecipeListAdapter
import com.minhoi.recipeapp.databinding.FragmentSearchBinding
import com.minhoi.recipeapp.ui.Dialog.FilterDialog
import com.minhoi.recipeapp.util.textChangesToFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    // 메뉴명, 재료명 으로 검색 가능하도록 구현 예정
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var myAdapter : RecipeListAdapter
    private var searchJob: Job? = null
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

        binding.apply {
            homeBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
            }

            mypageBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_searchFragment_to_mypageFragment)
            }

            refrigeratorBtn.setOnClickListener {
                it.findNavController().navigate(R.id.action_searchFragment_to_refrigeratorFragment)
            }
        }

        binding.inputRecipe.textChangesToFlow()
            .debounce(300L)
            .onEach { query ->
                // 새로운 입력이 있을 때마다 이전 검색 Job을 취소하고 새로운 Job 시작
                searchJob?.cancel()
                searchJob = lifecycleScope.launch(Dispatchers.IO) {
                    val list = viewModel.getRecipeName(query.toString())
                    withContext(Dispatchers.Main){
//                        myAdapter.setLists(list)
                    }
                }
            }
            .launchIn(lifecycleScope)


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

        myAdapter = RecipeListAdapter(requireContext()) {
            val intent = Intent(requireActivity(), RcpInfoActivity::class.java)
            intent.apply {
                putExtra("rcpSeq", it.rcp_SEQ)
            }
            startActivity(intent)
        }

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

