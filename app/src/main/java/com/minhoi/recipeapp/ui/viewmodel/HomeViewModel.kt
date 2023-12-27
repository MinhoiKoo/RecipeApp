package com.minhoi.recipeapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.minhoi.recipeapp.api.MyApi
import com.minhoi.recipeapp.api.RetrofitInstance
import com.minhoi.recipeapp.model.AppDatabase
import com.minhoi.recipeapp.model.ExpirationDateDao
import com.minhoi.recipeapp.model.ExpirationDateDto
import com.minhoi.recipeapp.model.ExpirationDateEntity
import com.minhoi.recipeapp.model.KakaoUserRepository
import com.minhoi.recipeapp.model.RecipeDataModel
import com.minhoi.recipeapp.model.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofitInstance : MyApi = RetrofitInstance.getInstance().create(MyApi::class.java)
    private val kakaoUserRepository = KakaoUserRepository()
    private val recipeRepository = RecipeRepository()

    private var _mutableRcpList = MutableLiveData<List<RecipeDataModel>>()
    val liveRcpList : LiveData<List<RecipeDataModel>>
        get() = _mutableRcpList

    private var _mutablePopularList = MutableLiveData<ArrayList<RecipeDataModel>>()
    val livePopularList : LiveData<ArrayList<RecipeDataModel>>
        get() = _mutablePopularList

    private var _mutableIngredientList = MutableLiveData<ArrayList<String>>()
    val liveIngredientList : LiveData<ArrayList<String>> = _mutableIngredientList
    private var tempIngredientList = arrayListOf<String>()

//    private var _mutableSelectIngredientList = MutableLiveData<ArrayList<SelectedIngredientDto>>()
//    val liveSelectIngredientList : LiveData<ArrayList<SelectedIngredientDto>> = _mutableSelectIngredientList

    private var _mutableSelectIngredientList = MutableLiveData<ArrayList<ExpirationDateDto>>()
    val liveSelectIngredientList: LiveData<ArrayList<ExpirationDateDto>> = _mutableSelectIngredientList

    private lateinit var userId: String

    suspend fun getUser() : String {
        return kakaoUserRepository.getUser()
    }

    private lateinit var dao: ExpirationDateDao

    init {
        _mutableIngredientList.value = tempIngredientList
        val database = AppDatabase.getDatabase(application)
        dao = database.expirationDateDao()
        viewModelScope.launch {
            setPopularRecipe()
            setRandomRecipe()
            getExpirationDatesForToday()
        }
    }

    suspend fun setRandomRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            val randomList = recipeRepository.getRandomRcp()
            withContext(Dispatchers.Main) {
                _mutableRcpList.value = randomList
            }
        }
    }

    private suspend fun setPopularRecipe() {
        viewModelScope.launch(Dispatchers.IO) {
            val popularList = recipeRepository.getPopularRcp()
            withContext(Dispatchers.Main) {
                _mutablePopularList.value = popularList
            }
        }
    }

    fun addIngredient(item : String) {
        tempIngredientList.add(item)
        _mutableIngredientList.value = tempIngredientList
    }

    fun deleteIngredient(position : Int) {
        val temp = _mutableSelectIngredientList.value ?: arrayListOf()
        temp.removeAt(position)
        _mutableSelectIngredientList.value = temp
    }


    fun isSelectIngredientListEmpty() : Boolean {
        return _mutableSelectIngredientList.value == null
    }


    fun setSelectedIngredientList(items : ArrayList<ExpirationDateDto>) {
        when(val currentIngredientList = _mutableSelectIngredientList.value) {
            null -> _mutableSelectIngredientList.value = items
            else -> {
                for(ingredientItem in items) {
                    if(!currentIngredientList.contains(ingredientItem)) {
                        currentIngredientList.add(ingredientItem)
                    }
                }
                _mutableSelectIngredientList.value = currentIngredientList!!
            }
        }
    }

    fun getSelectedIngredientName() : ArrayList<String> {
        val ingredientList = _mutableSelectIngredientList.value
        val nameList = arrayListOf<String>()
        for(item in ingredientList!!) {
            nameList.add(item.name)
        }
        return nameList
    }

    suspend fun saveResultListToDatabase(resultList: List<ExpirationDateDto>) {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
            for (expirationDateDto in resultList) {
                val expirationDateEntity = ExpirationDateEntity(
                    imagePath = expirationDateDto.imagePath,
                    name = expirationDateDto.name,
                    date = expirationDateDto.date
                )
                dao.insertExpirationDate(expirationDateEntity)
            }
        }
    }

    suspend fun getExpirationDatesForToday() {
        val todayDate = getCurrentDate()
        viewModelScope.launch(Dispatchers.IO) {
            val list = dao.getExpirationDatesForToday(todayDate)?.map {ExpirationDateDto(it.imagePath, it.name, it.date)}
            val l = arrayListOf<ExpirationDateDto>()
            if (list != null) {
                l.addAll(list)
            }
            withContext(Dispatchers.Main) {
                _mutableSelectIngredientList.value = l
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun deleteSelectedIngredient(item : ExpirationDateDto) {
        val currentList = _mutableSelectIngredientList.value ?: ArrayList()
        currentList.remove(item)
        _mutableSelectIngredientList.value = currentList
    }

}