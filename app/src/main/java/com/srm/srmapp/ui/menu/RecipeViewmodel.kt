package com.srm.srmapp.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.Resource
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.repository.recipes.RecipeRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewmodel @Inject constructor(private val recipeRepository: RecipeRepository, userSession: UserSession) : BaseViewModel(userSession) {
    private val _recipeList: MutableLiveData<Resource<List<Recipe>>> = MutableLiveData()
    val recipeList: LiveData<Resource<List<Recipe>>>
        get() = _recipeList

    private val _recipe: MutableLiveData<Resource<Recipe>> = MutableLiveData()
    val recipe: LiveData<Resource<Recipe>>
        get() = _recipe

    private val _recipeFood: MutableLiveData<Resource<List<Recipe.RecipeFood>>> = MutableLiveData()
    val recipeFood: LiveData<Resource<List<Recipe.RecipeFood>>>
        get() = _recipeFood

    init {
        Timber.d("INIT")
    }


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

    fun getRecipeFoodBy(id: Int) {
        fetchResource(_recipeFood) {
            recipeRepository.getRecipe(id).let {
                Timber.d(it.data?.food.toString())
                it.wrapThis(it.data?.food ?: emptyList())
            }
        }
    }

    fun addRecipe(recipe: Recipe) {
        fetchResource(_status, onSuccess = {
            refreshRecipeList()
        }) {
            recipeRepository.postRecipe(recipe)
        }
    }

    fun deleteRecipe(id: Int) {
        fetchResource(_status, onSuccess = {
            _recipeList.value?.data?.toMutableList()?.let { list ->
                list.removeIf { it.recipeId == id }
                _recipeList.postValue(Resource.Success(list.toList()))
            }
        }) {
            recipeRepository.deleteRecipe(id)
        }
    }

    fun putRecipe(recipe: Recipe) {
        fetchResource(_status) {
            recipeRepository.putRecipe(recipe)
        }
    }
}