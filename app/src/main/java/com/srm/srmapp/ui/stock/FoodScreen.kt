package com.srm.srmapp.ui.stock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart
import com.srm.srmapp.ui.theme.spacerWitdh
import timber.log.Timber
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@Composable
@Destination
fun FoodListScreen(
    navigator: DestinationsNavigator,
    viewmodel: StockViewmodel,
) {
    val foodListState by viewmodel.foodList.observeAsState(Resource.Empty())
    val stockList by viewmodel.stockList.observeAsState(Resource.Empty())
    val statusMessage by viewmodel.status.observeAsState(Resource.Empty())
    val refreshState = rememberSwipeRefreshState(foodListState.isLoading())
    var popupState by remember { mutableStateOf(false) }
    var popupAddFoodState by remember { mutableStateOf(false) }
    var popupSearchFood by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    var itemIdx by remember { mutableStateOf(-1) }
    val foodList = remember(foodListState.data) { foodListState.data ?: emptyList() }

    if (foodListState.isEmpty()) viewmodel.refreshFoodList()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(stringResource(R.string.food),
            onClickSearch = { popupSearchFood = true },
            onClickAdd = { popupAddFoodState = true },
            onClickBack = { navigator.navigateUp() })
        SwipeRefresh(
            state = refreshState,
            onRefresh = { viewmodel.refreshFoodList() }) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                stickyHeader {
                    Row(modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically) {
                        SrmText(text = stringResource(R.string.food_name))
                        SrmText(text = stringResource(R.string.quantity))
                    }
                }
                items(foodList, key = { it.foodId }) { food ->
                    FoodItem(food = food) { popupState = !popupState }
                    if (popupState)
                        FoodItemPopup(food = food,
                            onDismissRequest = { popupState = !popupState }, viewmodel = viewmodel)
                }
            }
        }
    }

    if (statusMessage.isSuccess() || statusMessage.isError()) {
        val msg = statusMessage.data ?: statusMessage.message
        msg?.let {
            SrmDialog(onDismissRequest = {
                viewmodel.clearStatus()
            }) {
                SrmText(text = it, textAlign = TextAlign.Center)
            }
        }
    }

    if (stockList.isSuccess()) {
        stockList.data?.let {
            SrmDialog(onDismissRequest = {
                viewmodel.clearStcokList()
            }) {
                if (it.isEmpty())
                    SrmText(text = "No stocks found")
                else {
                    LazyColumn(modifier = Modifier.wrapContentSize()) {
                        items(it, key = { it.stockId }) { stock ->
                            SrmSelectableRow() {
                                SrmText(text = "${stock.quantity} ${stock.expirationDate}", textAlign = TextAlign.Center)
                                IconButton(onClick = {
                                    viewmodel.deleteStock(stock)
                                }) {
                                    Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                                        contentDescription = stringResource(id = R.string.delete))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (popupAddFoodState) {
        var name by remember { mutableStateOf("") }
        var units by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("") }
        SrmDialog(onDismissRequest = {
            popupAddFoodState = false
            viewmodel.refreshFoodList()
        }) {
            SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
            SrmTextFieldHint(value = units, placeholder = stringResource(R.string.unit), onValueChange = { units = it })
            SrmTextFieldHint(value = type, placeholder = stringResource(R.string.category), onValueChange = { type = it })
            TextButton(onClick = {
                viewmodel.addFood(type, name, units)
                popupAddFoodState = false
            }) {
                SrmText(text = stringResource(R.string.add_food))
            }
        }
    }

    if (popupSearchFood) {
        SrmSearch(items = foodList,
            onDismissRequest = { popupSearchFood = false },
            predicate = { food, query ->
                food.name.startsWith(query, ignoreCase = true)
            }) { food ->
            SrmSelectableRow(
                onClick = {
                    foodList.indexOf(food).let { idx -> itemIdx = idx }
                    popupSearchFood = false
                }) {
                SrmText(text = food.name, textAlign = TextAlign.Center)
                SrmText(text = food.units, textAlign = TextAlign.Center)
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
fun FoodItem(food: Food, onClick: () -> Unit) {
    SrmSelectableRow(onClick = onClick, horizontalArrangement = Arrangement.SpaceEvenly) {
        SrmText(text = food.name, textAlign = TextAlign.Center)
        SrmText(text = food.units, textAlign = TextAlign.Center)
    }
}

@Composable
fun FoodItemPopup(
    food: Food,
    viewmodel: StockViewmodel,
    onDismissRequest: () -> Unit = {},
) {
    var popupAddStockState by remember { mutableStateOf(false) }
    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                viewmodel.getFoodStock(food)
                onDismissRequest.invoke()
            }) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_history_24), contentDescription = stringResource(R.string.show_stock))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.show_stock))
        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                popupAddStockState = true
                Timber.d("Add Stock $popupAddStockState")
            }) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), contentDescription = stringResource(R.string.add_stock))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.add_stock))
        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                viewmodel.deleteFood(food)
                onDismissRequest.invoke()
            }) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24), contentDescription = stringResource(id = R.string.delete))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.delete))
        }
    }


    if (popupAddStockState) {
        var quantity by remember { mutableStateOf("") }
        var date by remember { mutableStateOf(LocalDate.now()) }
        val error = try {
            quantity.toFloat()
            false
        } catch (e: Exception) {
            true
        }
        SrmDialog(onDismissRequest = {
            popupAddStockState = false
            onDismissRequest.invoke()
        }) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround) {
                SrmCalendarView(onDateSelected = { date = it })
            }
            SrmTextFieldHint(value = quantity,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                placeholder = stringResource(R.string.quantity),
                isError = error,
                onValueChange = { quantity = it })
            TextButton(enabled = !error, onClick = {
                viewmodel.addStock(food, quantity.toFloat(), date)
                popupAddStockState = false
                onDismissRequest.invoke()
            }) {
                SrmText(text = stringResource(R.string.add_food))
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun FoodItemPreview() {
}