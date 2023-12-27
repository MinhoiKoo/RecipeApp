package com.minhoi.recipeapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.minhoi.recipeapp.ui.ingredients.IngredientSelectActivity
import com.minhoi.recipeapp.R
import com.minhoi.recipeapp.ui.RecipeListActivity
import com.minhoi.recipeapp.util.ViewModelFactory
import com.minhoi.recipeapp.adapter.recyclerview.ExpirationDateListAdapter
import com.minhoi.recipeapp.adapter.recyclerview.SearchIngredientListAdapter
import com.minhoi.recipeapp.databinding.FragmentRefrigeratorBinding
import com.minhoi.recipeapp.model.ExpirationDateDto
import com.minhoi.recipeapp.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RefrigeratorFragment : Fragment() {
    private val TAG = RefrigeratorFragment::class.java.simpleName
    // Fragment 전환해도 Data 유지하기 위해 부모 액티비티 lifeCycle 따르는 viewModel로 선언
//    private val viewModel : HomeViewModel by activityViewModels()
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding : FragmentRefrigeratorBinding
    private lateinit var ingredientAdapter : ExpirationDateListAdapter
//    private lateinit var input : EditText
    private lateinit var inputIngredientAdapter: SearchIngredientListAdapter
    private val array = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refrigerator, container, false )
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))
            .get(HomeViewModel::class.java)
//        ingredientAdapter = SelectIngredientAdapter(requireContext()) {
//            //onClickListener
//            viewModel.deleteSelectedIngredient(it as SelectedIngredientDto)
//        }
        ingredientAdapter = ExpirationDateListAdapter(requireContext()) {
            viewModel.deleteIngredient(it)
        }

        val flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            //줄바꿈 설정
            flexWrap = FlexWrap.WRAP
            // item 정렬 기본 축
            flexDirection = FlexDirection.ROW
            // 정렬 기준
            justifyContent = JustifyContent.FLEX_START
        }


        binding.ingredientRv.apply {
            adapter = ingredientAdapter
            layoutManager = flexboxLayoutManager
        }

        binding.selectIngredientBtn.setOnClickListener {
            // 재료 선택 버튼 누르면 결과값 받아올 콜백 실행?
            val intent = Intent(requireActivity(), IngredientSelectActivity::class.java)
            getSelectedContent.launch(intent)
        }

//        input.addTextChangedListener(inputFormal)

        // 재료 추가 후 검색 버튼 누르면 재료 리스트를 Intent에 담아서 전달.
        binding.searchRefriBtn.setOnClickListener {
            when(viewModel.isSelectIngredientListEmpty()) {
                true -> {
                    Toast.makeText(requireContext(), "재료를 선택해주세요.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    searchRecipeByIngredient()
                }
            }
//            if (array.isNotEmpty()) {
//                val intent = Intent(activity, RecipeListActivity::class.java)
//                intent.putExtra("type", "ingredient")
//                intent.putExtra("ingredientList", array as ArrayList<String>)
//                startActivity(intent)
//            } else {
//                Toast.makeText(requireContext(), "재료를 입력해주세요.", Toast.LENGTH_LONG).show()
//            }
        }

        setObserve()
//            getRecipe(array) {
//                if(it.size != 0) {
//                    val intent = Intent(getActivity(), RecipeListActivity::class.java)
//                    intent.putExtra("recipeList", it)
//                    startActivity(intent)
//                }
//            }



        return binding.root
    }

    private fun setObserve() {
        viewModel.liveSelectIngredientList.observe(viewLifecycleOwner) {list ->
            if(list.isNotEmpty()) {
                binding.notSelectedLayout.visibility = View.GONE
                binding.selectedLayout.visibility = View.VISIBLE
            }
            else{
                binding.notSelectedLayout.visibility = View.VISIBLE
                binding.selectedLayout.visibility = View.GONE
            }

            ingredientAdapter.setList(list)
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.saveResultListToDatabase(list)
            }
        }
    }
    private val inputFormal = object : TextWatcher {
        // 공백이 생기면 재료 목록을 자동으로 추가해주는 TextWatcher
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if(s != null && s.toString().contains(" ")){
                val ingredient = s.toString().trim()

                // 첫 입력에 공백이 들어가면 사용자가 입력하지 못하게 Input Clear
                if(ingredient == "") {
//                    input.text.clear()
                } else {
                    viewModel.addIngredient(ingredient)
//                    input.text.clear()
//                    array.add(ingredient)
//                    ingredientAdapter.setIngredients(array)
                }
            }
        }
    }

    private fun searchRecipeByIngredient() {
        val selectedIngredientName = viewModel.getSelectedIngredientName()
        val intent = Intent(activity, RecipeListActivity::class.java)
        intent.putExtra("type", "ingredient")
        intent.putExtra("ingredientList", selectedIngredientName)
        startActivity(intent)
    }

    // 재료 선택 Activity 실행 후 결과 처리
    private val getSelectedContent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        // 콜백 등록 후 엑티비티에서 받아온 데이터 처리
        if (result.resultCode == Activity.RESULT_OK) {

            val data: Intent? = result.data
            data?.run {
                // 재료 선택 Activity에서 담은 재료 리스트 (SelectedIngredientDto)
                val resultList = data.getParcelableArrayListExtra<ExpirationDateDto>("SelectedIngredientList2")
                resultList?.let {
                    // ViewModel에 선택된 재료 주입
                    // db에 저장해야함

                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.saveResultListToDatabase(resultList)
                    }
                    viewModel.setSelectedIngredientList(resultList)
                }
            }
        }
    }
}