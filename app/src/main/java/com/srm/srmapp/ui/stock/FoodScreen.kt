package com.srm.srmapp.ui.stock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.theme.*

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

@Composable
@Destination
fun FoodListScreen(
    navigator: DestinationsNavigator,
    backStackEntry: NavBackStackEntry,
) {
    val viewmodel = hiltViewModel<StockViewmodel>(backStackEntry)
    val foodListState by viewmodel.foodList.observeAsState(Resource.Empty())
    val refreshState = rememberSwipeRefreshState(foodListState.isLoading())

    if (foodListState.isEmpty()) viewmodel.refreshFoodList()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(stringResource(R.string.food),
            onClickSearch = {},
            onClickAdd = {},
            onClickBack = { navigator.navigateUp() })
        SwipeRefresh(
            state = refreshState,
            onRefresh = { viewmodel.refreshFoodList() }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (foodListState.isSuccess()) {
                    foodListState.data?.let {
                        items(it) { food ->
                            FoodItem(food = food)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItem(food: Food) {
    var popupState by remember {
        mutableStateOf(false)
    }
    SrmSelectableRow(item = food) {
        SrmText(text = food.name, textAlign = TextAlign.Center)
        SrmText(text = food.units, textAlign = TextAlign.Center)
        IconButton(onClick = { popupState = !popupState }) {
            Icon(painter = painterResource(id = R.drawable.ic_baseline_menu_24), contentDescription = "Item Menu")
        }
    }
    if (popupState)
        FoodItemPopup(food = food) {
            popupState = !popupState
        }
}

@Composable
fun FoodItemPopup(
    food: Food,
    onDismissRequest: () -> Unit = {},
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmSelectableRow(item = food, horizontalArrangement = Arrangement.Start) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24), contentDescription = stringResource(id = R.string.delete))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.delete))
        }
        SrmSelectableRow(item = food, horizontalArrangement = Arrangement.Start) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_history_24), contentDescription = stringResource(id = R.string.delete))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.show_history))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodItemPreview() {
    FoodItem(Food(name = "Food", units = "Unit"))
}