package com.srm.srmapp.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.AppModule
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.theme.spacerWitdh
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination
fun OrderScreen(
    navigator: DestinationsNavigator,
    viewmodel: OrderViewModel,
) {
    val orderListState by viewmodel.orderList.observeAsState(Resource.Empty())
    if (orderListState.isEmpty()) viewmodel.refreshOrder()

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
    val orderList = remember(orderListState.data) { orderListState.data ?: emptyList() }

    var addDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(title = "Orders",
            onClickAdd = { addDialog = true },
            onClickBack = { navigator.popBackStack() },
            onClickSearch = { searchDialog = true })
        DropDownChangeStatus(text = stringResource(id = orderStatus.getStringId()), onStatusChange = {
            viewmodel.setOrderStatus(it)
            viewmodel.refreshOrder()
        })
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = orderListState.isLoading()),
            onRefresh = { viewmodel.refreshOrder() }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(orderList, key = { it.orderId }) {
                    OrderItem(order = it,
                        orderStatus = orderStatus,
                        onClick = {
                            viewmodel.getOrderRecipe(it)
                            orderDialog = true
                        })
                }
            }
        }
    }


    if (orderDialog && order.isSuccess()) {
        order.data?.let {
            OrderPopUp(
                it,
                onDismissRequest = {
                    orderDialog = false
                },
                onDelete = {
                    viewmodel.deleteOrder(it)
                    orderDialog = false
                },
                onStatusChange = { status ->
                    viewmodel.changeOrderStatus(it, status)
                },
                onModify = {
                    // TODO
                }
            )
        }
    }

    if (addDialog) {
        // TODO
    }

    if (searchDialog) {
        SrmSearch(items = orderList,
            label = "Buscar pedidos",
            onDismissRequest = { searchDialog = false },
            predicate = viewmodel.predicate) { order ->
            var dialogItemState by remember { mutableStateOf(false) }
            OrderItem(order = order, orderStatus = orderStatus, minimal = true) {
                dialogItemState = true
            }
            if (dialogItemState) {
                OrderPopUp(
                    order, onDismissRequest = { dialogItemState = false },
                    onDelete = {
                        viewmodel.deleteOrder(order)
                        dialogItemState = false
                    },
                    onStatusChange = { status ->
                        viewmodel.changeOrderStatus(order, status)
                    },
                    onModify = {
                        //TODO
                    }
                )
            }
        }
    }
}


@Composable
fun OrderPopUp(
    order: Order,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    onStatusChange: (Order.Status) -> Unit,
    onModify: () -> Unit,
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmText(text = "Plats: ")
        order.recipeList.forEachIndexed { index, recipe ->
            Column(modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()) {
                SrmText(text = "Nom ${recipe.recipeId}")
                SrmText(text = "Quantity ${recipe.quantity}")
                SrmText(text = "Tipus ${stringResource(id = Recipe.getResource(recipe.type))}")
            }
        }

        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = onDelete) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24), contentDescription = stringResource(id = R.string.delete))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.delete))
        }

        SrmSelectableRow(
            horizontalArrangement = Arrangement.Start,
            onClick = onModify) {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_edit_24), contentDescription = stringResource(id = R.string.modificar))
            Spacer(modifier = Modifier.width(spacerWitdh))
            SrmText(text = stringResource(R.string.modificar))
        }
        SrmSpacedRow {
            Spacer(modifier = Modifier.width(spacerWitdh))
            Icon(painter = painterResource(id = R.drawable.ic_baseline_edit_24), contentDescription = stringResource(id = R.string.modificar))
            Spacer(modifier = Modifier.width(spacerWitdh))
            DropDownChangeStatus(text = "Estat actual: ${stringResource(id = order.status.getStringId())}", onStatusChange = onStatusChange)
        }
    }
}

@Composable
fun DropDownChangeStatus(text: String, onStatusChange: (Order.Status) -> Unit) {
    SrmDropDownMenu(text = text, options = Order.Status.STAUS_UI,
        onClick = onStatusChange
    ) {
        SrmText(text = stringResource(id = it.getStringId()))
    }
}

@Composable
fun OrderItem(order: Order, orderStatus: Order.Status, minimal: Boolean = false, onClick: () -> Unit = {}) {
    val status: String = if (orderStatus is Order.Status.None) stringResource(id = order.status.getStringId()) else ""
    val date = order.booking?.date?.format(AppModule.dateTimeFormatter)
    val table = order.booking?.table ?: ""

    if (minimal)
        SrmListItem(startText = "${order.orderId}", endText = "Taula $table", onClick = onClick)
    else
        SrmListItem(startText = "${order.orderId}\nTaula: $table", endText = "$status\n$date", onClick = onClick)
}