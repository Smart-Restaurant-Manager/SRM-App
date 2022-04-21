package com.srm.srmapp.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.fetchResource
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.repository.recipes.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewmodel @Inject constructor(private val recipeRepository: RecipeRepository) : ViewModel() {
    init {
        Timber.d("INIT")
    }
    private val _recipeList: MutableLiveData<Resource<List<Recipe>>> = MutableLiveData()
    val recipeList: LiveData<Resource<List<Recipe>>>
        get() = _recipeList

    private val _recipe: MutableLiveData<Resource<Recipe>> = MutableLiveData()
    val recipe: LiveData<Resource<Recipe>>
        get() = _recipe

    private val _status: MutableLiveData<Resource<String>> = MutableLiveData()
    val status: LiveData<Resource<String>>
        get() = _status

    fun refreshRecipeList() {
        Timber.d("Call refresh")
        fetchResource(_recipeList) {
            recipeRepository.getRecipes()
        }
    }

    fun getRecipeBy(id: Int) {
        fetchResource(_recipe) {
            recipeRepository.getRecipe(id)
        }
    }

    fun addRecipe(recipe: Recipe) {
        fetchResource(_status) {
            recipeRepository.postRecipe(recipe)
        }
    }

    fun deleteRecipe(id: Int) {
        fetchResource(_status) {
            recipeRepository.deleteRecipe(id)
        }
    }

    fun putRecipe(recipe: Recipe) {
        fetchResource(_status) {
            recipeRepository.putRecipe(recipe)
        }
    }
}