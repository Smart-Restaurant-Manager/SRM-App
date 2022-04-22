package com.srm.srmapp.ui.stock

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
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
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.theme.*
import timber.log.Timber
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodMainScreen(navigator: DestinationsNavigator) {
    val buttonNames = listOf(
        R.string.carne,
        R.string.cereales,
        R.string.mariscos,
        R.string.vegetales,
        R.string.lacteos,
        R.string.especias,
    )
    SrmHeader(title = stringResource(id = R.string.title_stock)) { navigator.navigateUp() }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(buttonNames) { buttonName ->
                SrmButton(modifier = Modifier
                    .padding(padding)
                    .width(150.dp)
                    .height(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor1),
                    onClick = {
//                        navigator.navigate(FoodListScreenDestination(id = buttonName))
                    },
                    text = stringResource(id = buttonName))
            }
        }
    }
}

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

    if (foodListState.isEmpty()) viewmodel.refreshFoodList()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(stringResource(R.string.food),
            onClickSearch = {},
            onClickAdd = { popupAddFoodState = true },
            onClickBack = { navigator.navigateUp() })
        SwipeRefresh(
            state = refreshState,
            onRefresh = { viewmodel.refreshFoodList() }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (foodListState.isSuccess()) {
                    foodListState.data?.let {
                        stickyHeader {
                            SrmSpacedRow {
                                SrmText(text = stringResource(R.string.food_name))
                                SrmText(text = stringResource(R.string.quantity))
                                Spacer(modifier = Modifier.width(20.dp))
                            }
                        }
                        items(it, key = { it.foodId }) { food ->
                            FoodItem(food = food) { popupState = !popupState }
                            if (popupState)
                                FoodItemPopup(food = food,
                                    onDismissRequest = { popupState = !popupState }, viewmodel = viewmodel)
                        }
                    }
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
                            SrmSelectableRow(item = stock) {
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
        SrmDialog(onDismissRequest = { popupAddFoodState = false }) {
            SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
            SrmTextFieldHint(value = units, placeholder = stringResource(R.string.unit), onValueChange = { units = it })
            SrmTextFieldHint(value = type, placeholder = stringResource(R.string.category), onValueChange = { type = it })
            TextButton(onClick = {
                viewmodel.addFood(type, name, units)
                viewmodel.refreshFoodList()
                popupAddFoodState = false
            }) {
                SrmText(text = stringResource(R.string.add_food))
            }
        }
    }
}

@Composable
fun FoodItem(food: Food, onClick: () -> Unit) {
    SrmSelectableRow(item = food) {
        SrmText(text = food.name, textAlign = TextAlign.Center)
        SrmText(text = food.units, textAlign = TextAlign.Center)
        IconButton(onClick = onClick) {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_menu_24), contentDescription = "Item Menu")
        }
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
            item = food,
            horizontalArrangement = Arrangement.Start,
            onClick = {
                viewmodel.getFoodStock(it)
                onDismissRequest.invoke()
            }) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_history_24), contentDescription = stringResource(R.string.show_stock))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.show_stock))
        }
        SrmSelectableRow(
            item = food,
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
            item = food,
            horizontalArrangement = Arrangement.Start,
            onClick = {
                viewmodel.deleteFood(it)
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

@Composable
fun PlotStock(yData: List<Float>, xLabel: List<String>) {
    val textPaint = remember {
        Paint().apply {
            textSize = 40f
        }
    }
    val textPath = remember {
        Path()
    }

    val paint = remember {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 3.0f
            isAntiAlias = true
        }
    }

    val rectfList: MutableList<RectF> = remember {
//        calcRectangles(yData,)
        mutableListOf()
    }
    Canvas(modifier = Modifier.height(400.dp), onDraw = {
        for ((rect, label) in rectfList.zip(xLabel)) {
//            drawRoundRect(rect, 20f, 20f, paint)
//            drawTextOnPath(label, textPath.apply {
//                reset()
//                val center = rect.centerX() + textPaint.textSize / 2
//                moveTo(center, rect.bottom - 20)
//                lineTo(center, rect.top)
//            }, 0f, 0f, textPaint)
        }
    })
}

fun calcRectangles(yData: List<Float>, ww: Float, hh: Float): MutableList<RectF> {
    val maxY = yData.maxOfOrNull { it } ?: return mutableListOf()
    val barBottomPadding = 10f
    val barTopPadding = 10f
    val barWidht = ww / yData.size
    var xOffset = 0f
    val rectfList = mutableListOf<RectF>()
    for (y in yData) {
        val barHeight = (y / maxY) * hh - barTopPadding
        val rect = RectF(xOffset, hh - barHeight, xOffset + barWidht, hh - barBottomPadding)
        rectfList.add(rect)
        xOffset += barWidht
    }
    return rectfList
}

@Preview(showBackground = true)
@Composable
fun FoodItemPreview() {
}