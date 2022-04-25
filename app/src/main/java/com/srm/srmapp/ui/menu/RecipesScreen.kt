package com.srm.srmapp.ui.menu

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.srm.srmapp.ui.theme.ButtonColor2
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart
import com.srm.srmapp.ui.theme.spacerWitdh
import timber.log.Timber
import kotlin.math.log
import kotlin.reflect.typeOf

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
    // add item dialog state
    var dialogAddState by remember { mutableStateOf(false) }

    // Swipe to refresh satte
    val refreshState = rememberSwipeRefreshState(recipeListState.isLoading())

    // Search dialog state
    var dialogSearchRecipe by remember { mutableStateOf(false) }
    var itemIdx by remember { mutableStateOf(-1) }
    val recipeList = remember(recipeListState.data) { recipeListState.data ?: emptyList() }

    // Lazy list state
    val lazyListState = rememberLazyListState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        SrmAddTitleSearch(stringResource(id = R.string.entrantes),
            onClickSearch = { dialogSearchRecipe = true },
            onClickAdd = { dialogAddState = true },
            onClickBack = { navigator.navigateUp() })
        SwipeRefresh(
            state = refreshState,
            modifier = Modifier.padding(0.dp, 30.dp),
            onRefresh = { viewmodel.refreshRecipeList() }) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                stickyHeader {
                    Row(modifier = Modifier
                        .height(50.dp)
                        .background(Color.White)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(color = ButtonColor2, RoundedCornerShape(20))
                                .size(120.dp)
                                .fillMaxHeight()



                        ) {
                            SrmText(text = stringResource(R.string.recipe_name),color = Color.White, modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(color = ButtonColor2, RoundedCornerShape(20))
                                .size(120.dp)
                                .fillMaxHeight()



                        ) {
                            SrmText(text = stringResource(R.string.preu_euros),color = Color.White, modifier = Modifier.align(Alignment.Center)
                            )
                        }

                    }
                }
                items(recipeList.filter {
//                            it.type == recipeType // No implemented yet
                    true
                }, key = { it.id }) {
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



    if (dialogAddState) {
        AddRecipeDialog(onDismissRequest = {
            dialogAddState = false
        }, viewmodel, stockViewmodel, recipeType)
    }

    if (dialogSearchRecipe) {
        SrmSearch(items = recipeList,
            onDismissRequest = { dialogSearchRecipe = false },
            predicate = { recipeItem, query ->
                recipeItem.name.startsWith(query, ignoreCase = true)
            }) { recipeItem ->
            SrmSelectableRow(
                onClick = {
                    recipeList.indexOf(recipeItem).let { idx -> itemIdx = idx }
                    dialogSearchRecipe = false
                }) {
                SrmText(text = recipeItem.name, textAlign = TextAlign.Center)
                SrmText(text = recipeItem.price.toString(), textAlign = TextAlign.Center)
            }
        }
        if (itemIdx >= 0) {
            Timber.d("Scroll to $itemIdx")
            LaunchedEffect(key1 = itemIdx, block = {
                lazyListState.scrollToItem(itemIdx, 0)
            })
        }
    }

}


@Composable
fun AddRecipeDialog(onDismissRequest: () -> Unit, viewmodel: RecipeViewmodel, stockViewmodel: StockViewmodel, recipeType: Recipe.RecipeType) {
    var name by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    val selectedFood = remember { arrayListOf<Pair<Int, Float>>() }
    val foodList by stockViewmodel.foodList.observeAsState(Resource.Empty())

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
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {

    SrmSelectableRow(onClick = onClick, horizontalArrangement = Arrangement.SpaceEvenly) {
        SrmText(text = recipe.name, textAlign = TextAlign.Center)
        SrmText(text = recipe.price.toString(), textAlign = TextAlign.Center)
    }
}

@Composable
fun RecipeItemPopUp(
    recipe: Recipe,
    viewmodel: RecipeViewmodel,
    onDismissRequest: () -> Unit = {},
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
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
                Timber.d("Get Ingredients ${recipe.name}")
                 viewmodel.getRecipeBy(recipe.id)
                onDismissRequest.invoke()
            }) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), contentDescription = "Mostrar ingredients")
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.show_ingredients), )
        }
    }
}


@Composable
fun FoodSelector(foodList: List<Food>, onCheckedChange: (Food, Boolean, Float) -> Unit) {
    LazyColumn(Modifier.height(200.dp)) {
        items(foodList) { food ->
            var checked by remember { mutableStateOf(false) }
            var quantity by remember { mutableStateOf("") }
            SrmSelectableRow(horizontalArrangement = Arrangement.SpaceEvenly,
                onClick = {
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