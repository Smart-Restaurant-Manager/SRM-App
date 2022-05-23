package com.srm.srmapp.ui.order

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.AppModule
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Order
import com.srm.srmapp.data.models.Recipe
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
        searchLabel = "Buscar pedidos",
        startSearchText = { "${it.orderId}" },
        endSearchText = { "Taula ${it.booking?.table}" })

    // dialog content
    val crudDialogContent = SrmCrudDialogContent<Order>(
        editDialogContent = { order ->
            val recipeList by viewmodel.recipeList.observeAsState(Resource.Empty())
            if (recipeList.isEmpty()) viewmodel.refreshRecipeList()
            OrderDialogContent(orderState = order,
                buttonText = "Modify order",
                recipeList = recipeList.data ?: emptyList()) {
                viewmodel.putOrder(it)
            }
        },
        addDialogContent = { order ->
            val recipeList by viewmodel.recipeList.observeAsState(Resource.Empty())
            if (recipeList.isEmpty()) viewmodel.refreshRecipeList()
            OrderDialogContent(orderState = order,
                buttonText = "Add recipes",
                addRecipeMode = true,
                recipeList = recipeList.data ?: emptyList()) {
                viewmodel.putOrder(it)
            }
        },
        onDelete = { viewmodel.deleteOrder(it) },
        moreDialogContent = { order ->
            val textList: List<Pair<String, String>> = listOf(
                Pair("Order id", order.orderId.toString()),
                Pair("Booking id", order.bookingId.toString()),
                Pair("Table", order.booking?.table ?: "??"),
                Pair("Status", order.status.getString()),
                Pair("", ""),
            )
            SrmInfoList(infoList = textList)
            SrmLazyRow(itemListResource = Resource.Success(data = order.recipeList)) {
                SrmListItem(startText = "${it.name} ${it.quantity} * ${it.price}€", endText = "${it.quantity * it.price}€")
            }
        },
    )

    SrmListWithCrudActions(
        title = stringResource(id = R.string.pedidos),
        itemList = orderListState.data ?: emptyList(),
        onAddDialog = {
            val recipeList by viewmodel.recipeList.observeAsState(Resource.Empty())
            if (recipeList.isEmpty()) viewmodel.refreshRecipeList()
            OrderDialogContent(orderState = null,
                buttonText = "Add order",
                recipeList = recipeList.data ?: emptyList()) {
                viewmodel.postOrder(it)
            }
        },
        onBack = { navigator.navigateUp() },
        onRefresh = { viewmodel.refreshOrder() },
        refresState = rememberSwipeRefreshState(isRefreshing = orderListState.isLoading()),
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
            DropDownChangeStatus(text = orderStatus.getString(), onStatusChange = {
                viewmodel.setOrderStatus(it)
                viewmodel.refreshOrder()
            })
        })
}

@Composable
fun OrderDialogContent(
    orderState: Order? = null,
    recipeList: List<Recipe>,
    addRecipeMode: Boolean = false,
    buttonText: String,
    onClick: (Order) -> Unit,
) {
    var selectedFood = remember { orderState?.recipeList?.associate { Pair(it.recipeId, it.quantity.toFloat()) } ?: emptyMap() }
    var status by remember { mutableStateOf(orderState?.status ?: Order.Status.None()) }
    var bookingId by remember { mutableStateOf(orderState?.bookingId?.toString() ?: "") }

    if (!addRecipeMode) {
        if (orderState == null) {
            SrmTextField(value = bookingId, label = "Booking id", onValueChange = { bookingId = it })
        }

        orderState?.let {
            DropDownChangeStatus(text = status.getString(), onStatusChange = {
                status = it
            })
        }
    }
    SrmQuantitySelector(
        optionsList = recipeList,
        selectorState = selectedFood,
        onUpdate = { selectedFood = it },
    ) {
        SrmText(text = it.name)
    }

    SrmTextButton(onClick = {
        val order = Order(orderId = orderState?.orderId ?: -1,
            bookingId = orderState?.bookingId ?: bookingId.toInt(),
            booking = null,
            status = status,
            recipeList = selectedFood.toList().map { Order.OrderRecipe(recipeId = it.first, quantity = it.second.toInt()) }
        )
        onClick.invoke(order)
    }, text = buttonText)
}


@Composable
fun DropDownChangeStatus(text: String, onStatusChange: (Order.Status) -> Unit) {
    SrmDropDownMenu(text = text, options = Order.Status.STAUS_UI,
        onClick = onStatusChange
    ) {
        SrmText(text = it.getString())
    }
}