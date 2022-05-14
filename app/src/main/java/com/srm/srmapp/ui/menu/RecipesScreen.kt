package com.srm.srmapp.ui.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.stock.StockViewmodel
import com.srm.srmapp.ui.theme.spacerWitdh
import timber.log.Timber

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

    // add item dialog state
    var dialogAddState by remember { mutableStateOf(false) }
    var addRecipe: Boolean = false

    // Search dialog state
    var dialogSearchRecipe by remember { mutableStateOf(false) }
    val recipeList = remember(recipeListState.data) { recipeListState.data ?: emptyList() }

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        SrmAddTitleSearch(stringResource(id = Recipe.getResource(recipeType)),
            onClickSearch = { dialogSearchRecipe = true },
            onClickAdd = { dialogAddState = true },
            onClickBack = { navigator.navigateUp() })
        SwipeRefresh(
            state = rememberSwipeRefreshState(recipeListState.isLoading()),
            onRefresh = { viewmodel.refreshRecipeList() }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(recipeList.filter { it.type == recipeType }, key = { it.id }) {
                    var dialogItemState by remember { mutableStateOf(false) }
                    RecipeItem(recipe = it) { dialogItemState = true }
                    if (dialogItemState) {
                        RecipeItemPopUp(
                            recipe = it,
                            viewmodel = viewmodel,
                            onDismissRequest = { dialogItemState = false }
                        )
                    }
                }
            }
        }
    }

    val statusMessage by viewmodel.status.observeAsState(Resource.Empty())
    if (statusMessage.isSuccess() || statusMessage.isError()) {
        val msg = statusMessage.data ?: statusMessage.message
        msg?.let {
            val openDialog = remember { mutableStateOf(true) }
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        viewmodel.clearStatus()
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            openDialog.value = false
                            viewmodel.clearStatus()
                        })
                        { Text(text = "Confirmar") }
                    },
                    text = { Text(text = it) }
                )
            }

        }
    }


    if (dialogAddState) {
        AddRecipeDialog(onDismissRequest = {
            dialogAddState = false
        }, viewmodel, stockViewmodel, recipeType)
    }

    if (dialogSearchRecipe) {
        SrmSearch(items = recipeList,
            label = "Buscar recetas",
            onDismissRequest = { dialogSearchRecipe = false },
            predicate = { recipeItem, query ->
                recipeItem.name.startsWith(query, ignoreCase = true)
            }) { recipeItem ->
            var dialogItemState by remember { mutableStateOf(false) }
            RecipeItem(recipe = recipeItem, minimal = true) { dialogItemState = true }
            if (dialogItemState) {
                RecipeItemPopUp(
                    recipe = recipeItem,
                    viewmodel = viewmodel,
                    onDismissRequest = { dialogItemState = false }
                )
            }
        }
    }
}


@Composable
fun AddRecipeDialog(onDismissRequest: () -> Unit, viewmodel: RecipeViewmodel, stockViewmodel: StockViewmodel, recipeType: Recipe.RecipeType) {
    var name by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var selectedFood = remember { listOf<Pair<Int, Float>>() }
    val foodList by stockViewmodel.foodList.observeAsState(Resource.Empty())
    if (foodList.isEmpty()) stockViewmodel.refreshFoodList()

    if (foodList.isSuccess()) {
        SrmDialog(onDismissRequest = onDismissRequest) {
            SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
            SrmTextFieldHint(value = precio, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = stringResource(R.string.price), onValueChange = { precio = it })


            foodList.data?.let { foodList ->
                SrmQuantitySelector(optionsList = foodList.map { it.name }) {
                    selectedFood = it.toList().filter { it.second.compareTo(0f) > 0 }
                }
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
fun RecipeItem(recipe: Recipe, minimal: Boolean = false, onClick: () -> Unit) {
    if (minimal)
        SrmListItem(startText = recipe.name,
            endText = "${recipe.price}€",
            enableSelect = true,
            onClick = onClick)
    else
        SrmListItem(startText = "${recipe.name}\n${recipe.price}€",
            endText = "${recipe.id}\n" + if (recipe.available == true) "Disponible" else "No disponible",
            icon = painterResource(id = R.drawable.ic_baseline_image_not_supported_24),
            enableSelect = true,
            onClick = onClick)
}

@Composable
fun RecipeItemPopUp(
    recipe: Recipe,
    viewmodel: RecipeViewmodel,
    onDismissRequest: () -> Unit = {},
) {

    var popupSeeReceipt by remember { mutableStateOf(false) }
    var popupEditReceipt by remember { mutableStateOf(false) }


    SrmDialog(onDismissRequest = onDismissRequest) {
        Icon(modifier = Modifier.size(150.dp, 150.dp),
            painter = painterResource(id = R.drawable.ic_baseline_image_not_supported_24),
            contentDescription = "")
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                viewmodel.deleteRecipe(recipe.id)
                onDismissRequest.invoke()
            }) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24), contentDescription = stringResource(id = R.string.delete))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.delete))
        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                popupEditReceipt = true
            }
        ) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = stringResource(R.string.mod_menu))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.mod_menu))
        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                popupSeeReceipt = true

            }) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), contentDescription = "Mostrar ingredients")
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.show_ingredients))
        }
    }
    //Hablar con Pablo
    if (popupSeeReceipt) {
        SrmDialog(onDismissRequest = { popupSeeReceipt = false }) {
            Spacer(modifier = Modifier.size(20.dp))
            SrmText(text = "Ingrediente= ${recipe.food}")
            /*
            for (element in recipe.food!!){
                Spacer(modifier = Modifier.size(20.dp))
                SrmText(text = "Ingrediente ID:  ${element.first}   Cantidad: ${element.second}"  )

            }
            */

        }
    }

    if (popupEditReceipt) {

    }
}