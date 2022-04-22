package com.srm.srmapp.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.stock.StockViewmodel
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart
import timber.log.Timber


@Destination
@Composable
fun RecipeScreen(
    navigator: DestinationsNavigator,
    recipeType: Recipe.RecipeType,
    viewmodel: RecipeViewmodel = hiltViewModel(),
) {
    val recipeList by viewmodel.recipeList.observeAsState(Resource.Empty())
    val recipe by viewmodel.recipe.observeAsState(Resource.Empty())
    val status by viewmodel.status.observeAsState(Resource.Empty())
    var popupState by remember { mutableStateOf(false) }
    var popupAddState by remember { mutableStateOf(false) }
    val refreshState = rememberSwipeRefreshState(recipeList.isLoading())

    if (recipeList.isEmpty()) viewmodel.refreshRecipeList()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        SrmAddTitleSearch(stringResource(id = R.string.entrantes),
            onClickSearch = {},
            onClickAdd = { popupAddState = true },
            onClickBack = { navigator.navigateUp() })
        SwipeRefresh(
            state = refreshState,
            onRefresh = { viewmodel.refreshRecipeList() }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (recipeList.isSuccess())
                    recipeList.data?.let { recipeList ->
                        items(recipeList.takeWhile { it.type == recipeType }) { recipe ->
                            RecipeItem(recipe = recipe) { popupState = !popupState }
                            if (popupState)
                                RecipeItemPopUp(recipe = recipe,
                                    onDismissRequest = { popupState = !popupState })
                        }
                    }
            }
        }
    }


    if (popupAddState) {
        AddRecipeDialog(onDismissRequest = {
            popupAddState = false
        }, viewmodel, recipeType)
    }
}


@Composable
fun AddRecipeDialog(onDismissRequest: () -> Unit, viewmodel: RecipeViewmodel, recipeType: Recipe.RecipeType) {
    var name by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    val selectedFood = remember { arrayListOf<Pair<Int, Float>>() }
    val stockViewmodel: StockViewmodel = hiltViewModel()
    val foodList by stockViewmodel.foodList.observeAsState(Resource.Empty())
    if (foodList.isEmpty())
        stockViewmodel.refreshFoodList()

    if (foodList.isSuccess()) {
        SrmDialog(onDismissRequest = onDismissRequest) {
            SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
            SrmTextFieldHint(value = precio, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = stringResource(R.string.price), onValueChange = { precio = it })

            foodList.data?.let { foodList ->
                FoodSelector(foodList, onCheckedChange = { food, check, quantity ->
                    if (quantity <= 0) return@FoodSelector
                    if (check) {
                        selectedFood.removeIf { it.first == food.foodId }
                        selectedFood.add(Pair(food.foodId, quantity))
                    } else {
                        selectedFood.removeIf { it.first == food.foodId }
                    }
                    Timber.d(selectedFood.toString())
                })
            }

            SrmTextButton(onClick = {
                val recipe = Recipe(type = recipeType, name = name, price = precio.toFloatOrNull() ?: 0f, food = selectedFood)
                Timber.d(recipe.toString())
                viewmodel.addRecipe(recipe)
                onDismissRequest.invoke()
            }, text = stringResource(id = R.string.add_recipe))
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onclick: () -> Unit) {
    SrmSpacedRow(horizontalArrangement = Arrangement.SpaceEvenly) {
        SrmText(text = recipe.name, textAlign = TextAlign.Center)
        SrmText(text = recipe.price.toString(), textAlign = TextAlign.Center)
    }
}

@Composable
fun RecipeItemPopUp(
    recipe: Recipe,
    viewmodel: RecipeViewmodel = hiltViewModel(),
    onDismissRequest: () -> Unit = {},
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmSelectableRow(
            item = recipe,
            horizontalArrangement = Arrangement.Start,
            onClick = {
                viewmodel.getRecipeBy(it.id)
                onDismissRequest.invoke()
            }) {

        }
    }
}


@Composable
fun FoodSelector(foodList: List<Food>, onCheckedChange: (Food, Boolean, Float) -> Unit) {
    LazyColumn() {
        items(foodList) { food ->
            var checked by remember { mutableStateOf(false) }
            var quantity by remember { mutableStateOf("") }
            SrmSelectableRow(horizontalArrangement = Arrangement.SpaceEvenly,
                item = food, onClick = {
                    checked = !checked
                    onCheckedChange.invoke(food, checked, quantity.toFloatOrNull() ?: 0.0f)
                }) {
                if (checked) {
                    SrmTextFieldHint(modifier = Modifier.width(100.dp),
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = quantity,
                        placeholder = stringResource(id = R.string.quantity),
                        onValueChange = {
                            quantity = it
                            onCheckedChange.invoke(food, true, quantity.toFloatOrNull() ?: 0f)
                        }
                    )
                }
                SrmText(text = food.name)
            }
        }
    }
}