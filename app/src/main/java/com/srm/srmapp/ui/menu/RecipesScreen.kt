package com.srm.srmapp.ui.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.srm.srmapp.Utils.format
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.stock.StockViewmodel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    val recipeList = remember(recipeListState.data) { recipeListState.data ?: emptyList() }.let {
        it.filter { it.type == recipeType }
    }

    // Food list
    val a by stockViewmodel.foodList.observeAsState(Resource.Empty())
    if (a.isEmpty()) stockViewmodel.refreshFoodList()

    val lazyListState = rememberLazyListState()
    var searchIdx by remember { mutableStateOf(-1) }

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
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(recipeList, key = { _, it -> it.id }) { idx, recipeItem ->
                    var dialogItemState by remember { mutableStateOf(false) }
                    var modDialog by remember { mutableStateOf(false) }
                    var seeDialog by remember { mutableStateOf(false) }
                    if (idx == searchIdx) {
                        Card(border = BorderStroke(2.dp, Color.Red)) {
                            RecipeItem(recipe = recipeItem) { dialogItemState = true }
                        }
                        LaunchedEffect(key1 = searchIdx, block = {
                            delay(750)
                            searchIdx = -1
                        })
                    } else {
                        RecipeItem(recipe = recipeItem) { dialogItemState = true }
                    }

                    if (dialogItemState) {
                        SrmItemDialog(editText = stringResource(id = R.string.Editar),
                            deleteText = stringResource(id = R.string.mod_menu),
                            moreText = stringResource(id = R.string.show_ingredients),
                            onDismissRequest = { dialogItemState = false },
                            onClickEdit = { modDialog = true },
                            onClickDelete = { viewmodel.deleteRecipe(recipeItem.id) },
                            onClickMore = { seeDialog = true })
                    }

                    if (modDialog) {
                        RecipeDialog(buttonText = stringResource(id = R.string.mod_menu),
                            onDismissRequest = {
                                modDialog = false
                            }, onClick = {
                                viewmodel.putRecipe(it)
                            }, foodList = a.data ?: emptyList(),
                            recipeType = recipeType, recipeState = recipeItem)
                    }
                    if (seeDialog) {
                        //TODO
                    }
                }
            }
        }
    }


    if (dialogAddState) {
        RecipeDialog(buttonText = stringResource(id = R.string.add_recipe), onDismissRequest = {
            dialogAddState = false
        }, onClick = {
            viewmodel.addRecipe(it)
        }, foodList = a.data ?: emptyList(), recipeType = recipeType)
    }

    val scope = rememberCoroutineScope()
    if (dialogSearchRecipe) {
        SrmSearch(items = recipeList,
            label = "Buscar recetas",
            onDismissRequest = { dialogSearchRecipe = false },
            predicate = { recipeItem, query ->
                recipeItem.name.startsWith(query, ignoreCase = true)
            }) { recipeItem ->
            RecipeItem(recipe = recipeItem, minimal = true) {
                scope.launch {
                    searchIdx = recipeList.indexOfFirst { it.id == recipeItem.id }
                    Timber.d("searchidx $searchIdx ${recipeList[searchIdx]}")
                    lazyListState.scrollToItem(searchIdx)
                }
                dialogSearchRecipe = false
            }
        }
    }
}


@Composable
fun RecipeDialog(
    buttonText: String,
    onDismissRequest: () -> Unit,
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

    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
        SrmTextFieldHint(value = precio, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = stringResource(R.string.price), onValueChange = { precio = it })
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
                })
            onClick.invoke(recipe)
            onDismissRequest.invoke()
        }, text = buttonText)
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe, minimal: Boolean = false, onClick: () -> Unit,
) {
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