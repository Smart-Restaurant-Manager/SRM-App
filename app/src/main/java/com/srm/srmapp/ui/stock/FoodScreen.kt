package com.srm.srmapp.ui.stock

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.ui.common.SrmAddTitleSearch
import com.srm.srmapp.ui.common.SrmButton
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.common.SrmText
import com.srm.srmapp.ui.destinations.FoodListScreenDestination
import com.srm.srmapp.ui.theme.ButtonColor1

@Destination
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
                    .padding(30.dp)
                    .width(150.dp)
                    .height(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor1),
                    onClick = {
                        navigator.navigate(FoodListScreenDestination(id = buttonName))
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
    @StringRes id: Int,
) {
    val viewmodel = hiltViewModel<StockViewmodel>(backStackEntry)
    val foodListState by viewmodel.foodList.observeAsState(Resource.Empty())
    val refreshState = rememberSwipeRefreshState(foodListState.isLoading())

    if (foodListState.isEmpty()) viewmodel.refreshFoodList()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(stringResource(id = id),
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
    var selectedItem by remember { mutableStateOf(Food(name = "", units = "")) }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)) {
        Row(modifier = Modifier
            .fillMaxSize()
            .selectable(
                selected = selectedItem == food,
                onClick = { selectedItem = food }
            ),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {
            SrmText(text = food.name, textAlign = TextAlign.Center)
            SrmText(text = food.units, textAlign = TextAlign.Center)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodItemPreview() {
    FoodItem(Food(name = "Food", units = "Unit"))
}