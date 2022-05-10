package com.srm.srmapp.ui.order

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.ui.common.SrmAddTitleSearch
import com.srm.srmapp.ui.common.SrmDialog
import com.srm.srmapp.ui.common.SrmSelectableRow
import com.srm.srmapp.ui.common.SrmText
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun OrderScreen(
    navigator: DestinationsNavigator,
    viewmodel: OrderViewModel = hiltViewModel(),
) {
    val orderList by viewmodel.orderList.observeAsState(Resource.Empty())
    if (orderList.isEmpty()) viewmodel.refreshOrder()

    val order by viewmodel.order.observeAsState(Resource.Empty())
    val orderStatus by viewmodel.orderStatus.observeAsState(Order.Status.None())

    LaunchedEffect(key1 = orderStatus, block = {
        while (true) {
            delay(30.seconds)
            Timber.d("Refresh orders")
            viewmodel.refreshOrder()
        }
    })

    var orderDialog by remember { mutableStateOf(false) }
    var searchDialog by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        SrmAddTitleSearch(title = "Orders",
            showAdd = false,
            onClickBack = { navigator.popBackStack() },
            onClickSearch = { searchDialog = true })

        var dropdownStatus by remember { mutableStateOf(false) }

        DropDownChangeStatus(text = stringResource(id = orderStatus.getStringId()), onStatusChange = {
            viewmodel.setOrderStatus(it)
            viewmodel.refreshOrder()
        })

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = orderList.isLoading()),
            modifier = Modifier.padding(0.dp, 30.dp),
            onRefresh = { viewmodel.refreshOrder() }) {
            orderList.data?.let { orderList ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(orderList, key = { it.orderId }) {
                        OrderItem(order = it, orderStatus) {
                            viewmodel.getOrderRecipe(it)
                            orderDialog = true
                        }
                    }
                }
            }
        }
    }


    var recipeDialog by remember { mutableStateOf(false) }
    if (orderDialog && order.isSuccess()) {
        SrmDialog(onDismissRequest = {
            orderDialog = false
        }) {
            order.data?.let {
                it.recipeList.forEach { recipe ->
                    Column(modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .clickable {
                            viewmodel.getRecipe(recipeId = recipe.recipeId)
                            recipeDialog = true
                        }) {
                        SrmText(text = "Quantity ${recipe.quantity}")
                        SrmText(text = "Nom ${recipe.recipeId}")
                        SrmText(text = "Tipus ${stringResource(id = Recipe.getResource(recipe.type))}")
                    }
                }
            }
        }
    }

    val recipe by viewmodel.recipe.observeAsState(Resource.Empty())
    if (recipeDialog && recipe.isSuccess()) {
        SrmDialog(onDismissRequest = { recipeDialog = false }) {
            val foods = recipe.data?.food
            SrmText(text = recipe.data?.name ?: "Name not found")
            if (foods != null) {
                foods.forEach { (foodid, quantity) ->
                    SrmText(text = "Nom $foodid")
                    SrmText(text = "Quantity $quantity")
                }
            } else {
                SrmText(text = "No ingredients")
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownChangeStatus(text: String, onStatusChange: (Order.Status) -> Unit) {
    var dropdownStatus by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(modifier = Modifier.fillMaxWidth(),
        expanded = dropdownStatus,
        onExpandedChange = { dropdownStatus = !dropdownStatus }) {
        SrmText(textAlign = TextAlign.Center, text = text)
        ExposedDropdownMenu(expanded = dropdownStatus, onDismissRequest = { dropdownStatus = false }) {
            Order.Status.STAUS_UI.forEach {
                DropdownMenuItem(onClick = {
                    onStatusChange.invoke(it)
                    dropdownStatus = false
                }) {
                    SrmText(text = stringResource(id = it.getStringId()))
                }
            }
        }
    }
}

@Composable
fun OrderItem(order: Order, orderStatus: Order.Status, onClick: () -> Unit = {}) {
    SrmSelectableRow(onClick = onClick) {
        if (orderStatus !is Order.Status.None)
            SrmText(text = "${order.orderId}")
        else
            SrmText(text = "${order.orderId} ${order.status}")

    }
}