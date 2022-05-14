@file:OptIn(ExperimentalMaterialApi::class)

package com.srm.srmapp.ui.stock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.srm.srmapp.AppModule
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.ui.common.*
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
    // food list state
    val foodListState by viewmodel.foodList.observeAsState(Resource.Empty())
    if (foodListState.isEmpty()) viewmodel.refreshFoodList()

    // item add dialog state
    var dialogAddFoodState by remember { mutableStateOf(false) }

    // Search state dialog
    var dialogSearchFood by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val foodList = remember(foodListState.data) { foodListState.data ?: emptyList() }

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(stringResource(R.string.food),
            onClickSearch = { dialogSearchFood = true },
            onClickAdd = { dialogAddFoodState = true },
            onClickBack = { navigator.navigateUp() })
        SwipeRefresh(
            state = rememberSwipeRefreshState(foodListState.isLoading()),
            modifier = Modifier.padding(0.dp, 30.dp),
            onRefresh = { viewmodel.refreshFoodList() }) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                items(foodList, key = { it.foodId }) {
                    var dialogItemState by remember { mutableStateOf(false) }
                    FoodItem(food = it) { dialogItemState = true }
                    if (dialogItemState) {
                        FoodItemPopup(
                            food = it,
                            viewmodel = viewmodel,
                            onDismissRequest = { dialogItemState = false },
                        )
                    }
                }
            }
        }
    }


    // stock for one food
    var popupEditStock by remember { mutableStateOf(false) }

    val stockList by viewmodel.stockList.observeAsState(Resource.Empty())
    if (stockList.isSuccess()) {
        stockList.data?.let {
            SrmDialog(onDismissRequest = {
                viewmodel.clearStcokList()
            }) {
                if (it.isEmpty())
                    SrmText(text = "No hay stock ")
                else {
                    LazyColumn(modifier = Modifier.wrapContentSize()) {
                        items(it, key = { it.stockId }) { stock ->
                            SrmSelectableRow(onClick = {
                                popupEditStock = true
                            }) {
                                SrmText(text = " Cantidad: ${stock.quantity}    Fecha: ${stock.expirationDate.format(AppModule.dateFormatter)}",
                                    textAlign = TextAlign.Center)
                                IconButton(onClick = {
                                    viewmodel.deleteStock(stock)
                                }) {
                                    Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                                        contentDescription = stringResource(id = R.string.delete))
                                }
                            }

                            if (popupEditStock) {
                                val stocka = remember { StockDataHodle.fromStock(stock) }
                                StockDialog(resId = R.string.mod_stock,
                                    stock = stocka,
                                    onDismissRequest = { popupEditStock = false },
                                    onQuantityChange = { stocka.quantity = it },
                                    onDateChange = { stocka.date = it }) {
                                    viewmodel.putStock(stock.stockId, stocka)
                                }
                            }
                        }
                    }
                }
            }

        }

    }



    if (dialogAddFoodState) {
        var name by remember { mutableStateOf("") }
        var units by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("") }
        val options = Food.TYPES
        var expanded by remember { mutableStateOf(false) }
        SrmDialog(onDismissRequest = {
            dialogAddFoodState = false
        }) {
            SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
            SrmTextFieldHint(value = units, placeholder = stringResource(R.string.unit), onValueChange = { units = it })
            SrmDropDownMenu(text = "Categoria", options = options, onClick = { type = it }) {
                SrmText(text = it)
            }
            TextButton(onClick = {
                viewmodel.addFood(type, name, units)
                dialogAddFoodState = false
            }) {
                SrmText(text = stringResource(R.string.add_food))
            }
        }
    }

    if (dialogSearchFood) {
        SrmSearch(items = foodList,
            label = "Buscar alimentos",
            onDismissRequest = { dialogSearchFood = false },
            predicate = { food, query ->
                food.name.startsWith(query, ignoreCase = true)
            }) { food ->
            var dialogItemState by remember { mutableStateOf(false) }
            SrmSelectableRow(
                onClick = { dialogItemState = true }) {
                SrmText(text = food.name, textAlign = TextAlign.Center)
                SrmText(text = food.units, textAlign = TextAlign.Center)
            }
            if (dialogItemState) {
                FoodItemPopup(
                    food = food,
                    viewmodel = viewmodel,
                    onDismissRequest = { dialogItemState = false },
                )
            }
        }

    }
}


@Composable
fun StockDialog(
    resId: Int,
    stock: StockDataHodle,
    onDismissRequest: () -> Unit,
    onQuantityChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onClick: () -> Unit,
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmTextFieldHint(value = stock.date.toString(), placeholder = stringResource(R.string.date), readOnly = true, onValueChange = onDateChange)
        SrmTextFieldHint(value = stock.quantity.toString(), placeholder = stringResource(R.string.table), onValueChange = onQuantityChange)
        SrmTextButton(text = stringResource(resId), onClick = {
            onClick.invoke()
            onDismissRequest.invoke()
        })
    }
}

@Composable
fun FoodItem(food: Food, onClick: () -> Unit) {
    SrmListItem(startText = "${food.name}\n${20}${food.units}", endText = "${food.type}", onClick = onClick)
}

@Preview(showBackground = true)
@Composable
fun It() {
    FoodItem(food = Food("Type", 12, "name", "unit")) {

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
                Timber.d("Get food stock $food")
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
                Timber.d("Add Stock $food")
            }) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), contentDescription = stringResource(R.string.add_stock))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.add_stock))
        }
        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = {
                Timber.d("Delete $food")
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