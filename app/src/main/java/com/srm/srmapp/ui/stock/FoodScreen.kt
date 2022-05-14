package com.srm.srmapp.ui.stock

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.ui.common.*


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
        addDialogContent = {
            SrmText(text = "TODO add stock")
        },
        deleteDialogContent = {
            SrmText(text = "TODO delete food")
        },
        moreDialogContent = {
            SrmText(text = "TODO show stock")
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
        itemKey = { it.foodId },
        listItemStartText = { "${it.name}\n${it.units}" },
        listItemEndText = { "${it.type}\n${it.foodId}" },
        crudDialogContent = crudDialogContent,
        searchProperties = searchProperties,
        baseViewModel = viewmodel
    )
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
    SrmTextFieldHint(
        value = nombre,
        placeholder = stringResource(R.string.food_name),
        onValueChange = { nombre = it })
    SrmTextFieldHint(
        value = unidades,
        placeholder = stringResource(R.string.unidades),
        onValueChange = { unidades = it })

    SrmDropDownMenu(text = type, options = Food.TYPES, onClick = { type = it }) {
    }
    SrmTextButton(
        onClick = {
            val food = Food(type = type, foodId = foodState?.foodId ?: -1, name = nombre, units = unidades)
            onClick.invoke(food)
        },
        text = buttonText)
}