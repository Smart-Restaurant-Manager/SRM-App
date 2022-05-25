package com.srm.srmapp.ui.menu

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
        searchPredicate = { recipeItem, query ->
            recipeItem.recipeId.toString().startsWith(query, ignoreCase = true) ||
                    recipeItem.name.startsWith(query, ignoreCase = true)
        },
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
        onDelete = { viewmodel.deleteRecipe(it.recipeId) },
        moreDialogContent = {
            val rec by viewmodel.recipeFood.observeAsState(Resource.Empty())
            if (rec.isEmpty()) viewmodel.getRecipeFoodBy(it.recipeId)
            SrmInfoList(infoList = listOf(
                Pair("Nombre", it.name),
                Pair("Id", it.recipeId.toString()),
                Pair("Precio", it.price.format(2)),
                Pair("Tipo", Food.TYPES[it.foodType]),
            ))
            SrmLazyRow(itemListResource = rec) { item ->
                SrmListItem(startText = "Ingrediente: ${item.name}\n\nCantidad: ${item.quantity}  ${item.unit}", enableSelect = false)
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
        icon = painterResource(id = R.drawable.ic_baseline_image_not_supported_24),
        listItemStartText = { "${it.name}\n${it.price}€" },
        listItemEndText = { "${it.recipeId}\n" + if (it.available) "Disponible" else "No disp." },
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
    var type by remember { mutableStateOf(recipeState?.foodType?.let { Food.TYPES.getOrNull(it) } ?: Food.TYPES.first()) }
    var precio by remember { mutableStateOf(recipeState?.price?.format(2) ?: "") }
    var selectedFood = remember { recipeState?.food?.associate { Pair(it.foodId, it.quantity) } ?: emptyMap() }
    var available by remember { mutableStateOf(recipeState?.available ?: false) }

    SrmTextField(value = name, label = stringResource(R.string.food_name), onValueChange = { name = it })
    SrmTextField(value = precio, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = stringResource(R.string.price), onValueChange = { precio = it })
    SrmDropDownMenu(text = type, options = Food.TYPES, onClick = { type = it }) {
        SrmText(text = it)
    }
    SrmCheckBox(text = stringResource(if (available) R.string.available else R.string.unavailable), checkState = available) { available = it }

    SrmQuantitySelector(
        optionsList = foodList,
        selectorState = selectedFood,
        onUpdate = { selectedFood = it },
    ) {
        SrmText(text = it.name)
    }

    SrmTextButton(onClick = {
        val recipe = Recipe(
            recipeId = recipeState?.recipeId ?: -1,
            type = recipeType,
            name = name,
            foodType = Food.TYPES.indexOf(type),
            price = precio.toFloatOrNull() ?: 0f,
            food = selectedFood.toList().map { Recipe.RecipeFood(foodId = it.first, quantity = it.second) },
            available = available)
        onClick.invoke(recipe)
    }, text = buttonText)
}