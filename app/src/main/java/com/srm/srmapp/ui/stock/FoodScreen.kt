package com.srm.srmapp.ui.stock

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.AppModule
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.ui.common.*
import java.lang.Float.parseFloat
import java.time.LocalDate


@Composable
@Destination
fun FoodListScreen(
    navigator: DestinationsNavigator,
    viewmodel: StockViewmodel,
) {
    // food list state
    val foodListState by viewmodel.foodList.observeAsState(Resource.Empty())
    if (foodListState.isEmpty()) viewmodel.refreshFoodList()

    val foodList = remember(foodListState.data) { (foodListState.data ?: emptyList()) }

    //Search engine properties
    val searchProperties = SrmSearchProperties<Food>(
        searchPredicate = { foodItem, query -> foodItem.name.startsWith(query, ignoreCase = true) },
        indexPredicate = { it, found -> it.foodId == found.foodId },
        searchLabel = "Buscar Alimento",
        startSearchText = { it.name },
        endSearchText = { it.units })

    //dialog content
    val crudDialogContent = SrmCrudDialogContent<Food>(
        editDialogContent = { item ->
            FoodDialog(
                buttonText = stringResource(R.string.edit_food),
                onClick = { viewmodel.putFood(it) },
                foodState = item
            )
        },
        addDialogContent = { item ->
            StockDialog(
                buttonText = stringResource(R.string.add_stock),
                onClick = { viewmodel.addStock(item, it.quantity, it.expirationDate) },
                foodState = item.foodId,
                stockState = null
            )
        },
        onDelete = { viewmodel.deleteFood(it) },
        moreDialogContent = {
            val l by viewmodel.stockList.observeAsState(Resource.Empty())
            if (l.isEmpty()) viewmodel.getFoodStock(it)
            SrmLazyRow(itemListResource = l) { item ->
                val expired: String = if (item.expirationDate > LocalDate.now()) "Expired"
                else item.expirationDate.format(AppModule.dateFormatter)
                var editDialog by remember { mutableStateOf(false)}

                SrmListItem(startText = "${item.quantity} ${it.units}",
                    endText = expired,
                    onClick = {
                            editDialog = true
                    },
                    enableSelect = true,
                    endContent = {

                        var deleteDialog by remember { mutableStateOf(false) }
                        SrmIconButton(painter = painterResource(id = R.drawable.ic_baseline_delete_24)) {
                            deleteDialog = true
                        }

                        if (deleteDialog)
                            SrmDeleteDialog(onDismissRequest = { deleteDialog = false }) {
                                viewmodel.deleteStock(item)
                            }
                    })
                if(editDialog){
                    StockDialog(buttonText = stringResource(R.string.EditarStock),
                        onClick = {viewmodel.putStock(it)},
                        stockState = item,
                        foodState = item.foodId
                    )
                }
            }
        }
    )


    SrmListWithCrudActions(
        title = stringResource(id = R.string.food),
        itemList = foodList,
        onAddDialog = {
            FoodDialog(
                buttonText = stringResource(id = R.string.add_food),
                onClick = { viewmodel.addFood(it) },
            )
        },
        onBack = { navigator.navigateUp() },
        onRefresh = { viewmodel.refreshFoodList() },
        refresState = rememberSwipeRefreshState(isRefreshing = foodListState.isLoading()),
        listItemStartText = { "${it.name}\n ${it.stockCount} ${it.units}" },
        listItemEndText = { "${it.type}\n${it.foodId}" },
        crudDialogContent = crudDialogContent,
        searchProperties = searchProperties,
        baseViewModel = viewmodel
    )
}

@Composable
fun StockDialog(
    buttonText: String,
    onClick: (Stock) -> Unit,
    stockState: Stock? = null,
    foodState: Int? = null,
) {
    var unidades by remember { mutableStateOf(stockState?.quantity ?: "") }
    var caducidad by remember { mutableStateOf(stockState?.expirationDate ?: LocalDate.now()) }
    var error by remember { mutableStateOf(false) }
    SrmTextField(
        value = unidades.toString(),
        label = stringResource(R.string.Unidades),
        onValueChange = { unidades = it })
    SrmDateEditor(value = caducidad,
        label = stringResource(id = R.string.caducidad),
        onErrorAction = {
            error = true
        },
        onValueChange = {
            caducidad = it
            error = false
        })
    SrmTextButton(
        onClick = {
            val stock = Stock(stockId = stockState?.stockId ?: -1,
                foodId = foodState ?: -1,
                quantity = parseFloat(unidades.toString()),
                expirationDate = caducidad
            )
            onClick.invoke(stock)
        },
        enabled = !error,
        text = buttonText)
}


@Composable
fun FoodDialog(
    buttonText: String,
    onClick: (Food) -> Unit,
    foodState: Food? = null,
) {
    var nombre by remember { mutableStateOf(foodState?.name ?: "") }
    var unidades by remember { mutableStateOf(foodState?.units ?: "") }
    var type by remember { mutableStateOf(foodState?.type ?: "") }
    SrmTextField(
        value = nombre,
        label = stringResource(R.string.food_name),
        onValueChange = { nombre = it })
    SrmTextField(
        value = unidades,
        label = stringResource(R.string.unidades),
        onValueChange = { unidades = it })

    SrmDropDownMenu(text = type, options = Food.TYPES, onClick = { type = it }) {
        SrmText(text = it)
    }
    SrmTextButton(
        onClick = {
            val food = Food(type = type, foodId = foodState?.foodId ?: -1, name = nombre, units = unidades)
            onClick.invoke(food)
        },
        text = buttonText)
}

