package com.srm.srmapp.ui.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.AppModule
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.ui.common.*

@Composable
@Destination
fun OrderScreen(
    navigator: DestinationsNavigator,
    viewmodel: OrderViewModel,
) {
    val orderListState by viewmodel.orderList.observeAsState(Resource.Empty())
    if (orderListState.isEmpty()) viewmodel.refreshOrder()

    val orderStatus by viewmodel.orderStatus.observeAsState(Order.Status.None())


    // search engine properties
    val searchProperties = SrmSearchProperties<Order>(
        searchPredicate = viewmodel.predicate,
        indexPredicate = { it, found -> it.orderId == found.orderId },
        searchLabel = "Buscar pedidos",
        startSearchText = { "${it.orderId}" },
        endSearchText = { "Taula ${it.booking?.table}" })

    // dialog content
    val crudDialogContent = SrmCrudDialogContent<Order>(
        editDialogContent = { item ->
            // TODO
            SrmText(text = "Todo")
        },
        addDialogContent = {
            // TODO set to null to disable
            SrmText(text = "Todo")
        },
        deleteDialogContent = {
            // TODO confirmation with yes/no
            SrmText(text = "Todo")
        },
        moreDialogContent = {
            // TODO
            SrmText(text = "Todo")
        },
    )

    SrmListWithCrudActions(
        title = stringResource(id = R.string.pedidos),
        itemList = orderListState.data ?: emptyList(),
        onAddDialog = {
            // TODO
            SrmText(text = "Todo")
        },
        onBack = { navigator.navigateUp() },
        onRefresh = { viewmodel.refreshOrder() },
        refresState = rememberSwipeRefreshState(isRefreshing = orderListState.isLoading()),
        itemKey = { it.orderId },
        listItemStartText = { "${it.orderId}\nTaula: ${it.booking?.table}" },
        listItemEndText = {
            val status: String = if (orderStatus is Order.Status.None) it.status.toString() else ""
            val date = it.booking?.date?.format(AppModule.dateTimeFormatter)
            "${status}\n${date}"
        },
        searchProperties = searchProperties,
        crudDialogContent = crudDialogContent,
        baseViewModel = viewmodel,
        contentBefore = {
            DropDownChangeStatus(text = stringResource(id = orderStatus.getStringId()), onStatusChange = {
                viewmodel.setOrderStatus(it)
                viewmodel.refreshOrder()
            })
        })
}


@Composable
fun DropDownChangeStatus(text: String, onStatusChange: (Order.Status) -> Unit) {
    SrmDropDownMenu(text = text, options = Order.Status.STAUS_UI,
        onClick = onStatusChange
    ) {
        SrmText(text = stringResource(id = it.getStringId()))
    }
}