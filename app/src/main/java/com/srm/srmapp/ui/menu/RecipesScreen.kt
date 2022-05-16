package com.srm.srmapp.ui.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.format
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.stock.StockViewmodel

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun RecipeScreen(
    navigator: DestinationsNavigator,
    recipeType: Recipe.RecipeType,
    viewmodel: RecipeViewmodel,
    stockViewmodel: StockViewmodel,
) {
    // recipe list state
    val recipeListState by viewmodel.recipeList.observeAsState(Resource.Empty())
    if (recipeListState.isEmpty()) viewmodel.refreshRecipeList()

    // Food list
    val a by stockViewmodel.foodList.observeAsState(Resource.Empty())
    if (a.isEmpty()) stockViewmodel.refreshFoodList()

    val recipelist = remember(recipeListState.data) { (recipeListState.data ?: emptyList()).filter { it.type == recipeType } }

    // search engine properties
    val searchProperties = SrmSearchProperties<Recipe>(
        searchPredicate = { recipeItem, query -> recipeItem.name.startsWith(query, ignoreCase = true) },
        indexPredicate = { it, found -> it.id == found.id },
        searchLabel = "Buscar recetas",
        startSearchText = { it.name },
        endSearchText = { "${it.price}€" })


    // dialog content
    val crudDialogContent = SrmCrudDialogContent<Recipe>(
        editDialogContent = { item ->
            RecipeDialog(buttonText = stringResource(id = R.string.mod_menu),
                onClick = { viewmodel.putRecipe(it) },
                foodList = a.data ?: emptyList(),
                recipeType = recipeType,
                recipeState = item)
        },
        addDialogContent = null,
        onDelete = { viewmodel.deleteRecipe(it.id) },
        moreDialogContent = {
            val rec by viewmodel.recipeList.observeAsState(Resource.Empty())
            if (rec.isEmpty()) viewmodel.getRecipeBy(it.id)
            SrmLazyRow(itemListResource = rec) { item ->
                SrmListItem(startText = "${it.food?.get(0)} ${it.food?.get(1)}  ", enableSelect = false)

            }
        },
    )

    SrmListWithCrudActions(
        title = stringResource(id = Recipe.getResource(recipeType)),
        itemList = recipelist,
        onAddDialog = {
            RecipeDialog(buttonText = stringResource(id = R.string.add_recipe),
                onClick = { viewmodel.addRecipe(it) },
                foodList = a.data ?: emptyList(),
                recipeType = recipeType)
        },
        onBack = { navigator.navigateUp() },
        onRefresh = { viewmodel.refreshRecipeList() },
        refresState = rememberSwipeRefreshState(isRefreshing = recipeListState.isLoading()),
        itemKey = { it.id },
        icon = painterResource(id = R.drawable.ic_baseline_image_not_supported_24),
        listItemStartText = { "${it.name}\n${it.price}€" },
        listItemEndText = { "${it.id}\n" + if (it.available == true) "Disponible" else "No disp." },
        searchProperties = searchProperties,
        crudDialogContent = crudDialogContent,
        baseViewModel = viewmodel)
}


@Composable
fun RecipeDialog(
    buttonText: String,
    onClick: (Recipe) -> Unit,
    foodList: List<Food>,
    recipeType: Recipe.RecipeType,
    recipeState: Recipe? = null,
) {
    var name by remember { mutableStateOf(recipeState?.name ?: "") }
    var precio by remember { mutableStateOf(recipeState?.price?.format(2) ?: "") }
    var selectedFood = remember {
        recipeState?.food?.map { (foodid, quantity) ->
            Pair(foodList.indexOfFirst { it.foodId == foodid }, quantity)
        } ?: listOf()
    }
    var available by remember { mutableStateOf(recipeState?.available ?: false) }
    SrmTextField(value = name, label = stringResource(R.string.food_name), onValueChange = { name = it })
    SrmTextField(value = precio, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = stringResource(R.string.price), onValueChange = { precio = it })
    SrmCheckBox(text = stringResource(if (available) R.string.available else R.string.unavailable), checkState = available) { available = it }

    SrmQuantitySelector(optionsList = foodList.map { it.name }, // TODO Fix food, quantity not working
        selectorState = selectedFood.toTypedArray()) {
        selectedFood = it.toList().filter { it.second.compareTo(0f) > 0 }
    }

    SrmTextButton(onClick = {
        val recipe = Recipe(type = recipeType,
            name = name,
            price = precio.toFloatOrNull() ?: 0f,
            food = selectedFood.map { (idx, quantity) ->
                Pair(foodList[idx].foodId, quantity)
            },
            available = available)
        onClick.invoke(recipe)
    }, text = buttonText)
}