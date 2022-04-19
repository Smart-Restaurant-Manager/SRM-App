package com.srm.srmapp.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.launchException
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.repository.recipes.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewmodel @Inject constructor(private val RecipeRepository: RecipeRepository) : ViewModel() {
    init {
        Timber.d("INIT")
    }

    private val _recipeList: MutableLiveData<Resource<List<Recipe>>> = MutableLiveData()
    fun getRecipeListLiveData() = _recipeList as LiveData<Resource<List<Recipe>>>

    private val _recipe: MutableLiveData<Resource<Recipe>> = MutableLiveData()
    fun getRecipeLiveData() = _recipe as LiveData<Resource<Recipe>>

    private val _stockList: MutableLiveData<Resource<List<Stock>>> = MutableLiveData()
    fun getStockListLiveData() = _stockList as LiveData<Resource<List<Stock>>>

    private val _stock: MutableLiveData<Resource<Stock>> = MutableLiveData()
    fun getStockLiveData() = _stock as LiveData<Resource<Stock>>

    private val _foodList: MutableLiveData<Resource<List<Food>>> = MutableLiveData()
    fun getFoodListLiveData() = _foodList as LiveData<Resource<List<Food>>>

    private val _food: MutableLiveData<Resource<Food>> = MutableLiveData()
    fun getFoodLiveData() = _food as LiveData<Resource<Food>>


    fun refreshRecipeList() {
        Timber.d("Call refresh")
        _recipeList.value = Resource.Loading()
        viewModelScope.launchException {
            val recipe = RecipeRepository.getRecipes()
            _recipeList.postValue(recipe)
        }
    }

    fun getRecipeBy(id: Int) {
        viewModelScope.launchException {
            val recipe = RecipeRepository.getRecipe(id)
            _recipe.postValue(recipe)
        }
    }


    fun addRecipe(recipe: Recipe) {
        viewModelScope.launchException {
            // TODO
        }
    }

}