package com.srm.srmapp.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.srm.srmapp.data.models.GetId
import com.srm.srmapp.ui.bookings.BookingViewModel
import com.srm.srmapp.ui.menu.RecipeViewmodel
import com.srm.srmapp.ui.order.OrderViewModel
import com.srm.srmapp.ui.stock.StockViewmodel
import com.srm.srmapp.ui.theme.delayDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

data class SrmCrudDialogContent<T>(
    // Dialog content, set to null to disable
    val editLabel: String = "Modificar",
    val editDialogContent: (@Composable ColumnScope.(item: T) -> Unit)? = null,

    val deleteLabel: String = "Eliminar",
    val onDelete: ((T) -> Unit)? = null,

    val moreLabel: String = "Mes",
    val moreDialogContent: (@Composable ColumnScope.(item: T) -> Unit)? = null,

    val addLabel: String = "Afegir",
    val addDialogContent: (@Composable ColumnScope.(item: T) -> Unit)? = null,
)

data class SrmSearchProperties<T : GetId>(
    // Search parameters
    val searchPredicate: (T, String) -> Boolean,

    // Search Item view
    val searchLabel: String = "",
    val startSearchText: @Composable (T) -> String = { "" },
    val endSearchText: @Composable (T) -> String = { "" },

    // Search Item Click
    val onSearchItemClick: (T) -> Unit = { },
)

@Composable
fun <T : GetId> SrmListWithCrudActions(
    title: String,

    // list to display
    itemList: List<T>,

    // back button
    onBack: () -> Unit,

    // Add dialog content, set to null to disable
    onAddDialog: (@Composable (ColumnScope.() -> Unit))? = null,

    // Swipe refresh parameters
    onRefresh: () -> Unit,
    refresState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false),

    // list item parameters
    icon: Painter? = null,
    listItemStartText: @Composable (T) -> String = { "item" },
    listItemEndText: @Composable (T) -> String = { "" },

    // Dialog content
    crudDialogContent: SrmCrudDialogContent<T>,

    // Search properties
    searchProperties: SrmSearchProperties<T>?,

    // TODO implement roles
    baseViewModel: BaseViewModel,

    // Aditional content
    contentBefore: @Composable ColumnScope.() -> Unit = {},
    contentAfter: @Composable ColumnScope.() -> Unit = {},
) {

    val userPermissions = remember {
        // TODO UPDATE THESE VALUES
        if (baseViewModel.getRole() == 0) {
            Timber.d("User is manager")
            // add button, add item, edit item, more item, delete item
            arrayOf(true, true, true, true, true)
        } else {
            Timber.d("User is worker")
            when (baseViewModel) {
                is OrderViewModel -> {
                    Timber.d("Order roles")
                    arrayOf(true, true, true, true, true)
                }
                is StockViewmodel -> {
                    Timber.d("Stock roles")
                    arrayOf(true, true, true, true, true)
                }
                is RecipeViewmodel -> {
                    Timber.d("Recipe roles")
                    arrayOf(true, true, true, true, true)
                }
                is BookingViewModel -> {
                    Timber.d("Booking roles")
                    arrayOf(true, true, true, true, true)
                }
                else -> {
                    Timber.e("Unkown viewmodel!!")
                    arrayOf(false, false, false, false, false)
                }
            }
        }
    }

    val lazyListState = rememberLazyListState()
    var searchIdx by remember { mutableStateOf(-1) }
    var dialogSearchRecipe by remember { mutableStateOf(false) }
    var dialogAddState by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        SrmAddTitleSearch(title = title,
            onClickSearch = { dialogSearchRecipe = true },
            onClickAdd = { dialogAddState = true },
            onClickBack = onBack,
            enableAdd = onAddDialog != null,
            enableSearch = searchProperties != null)
        contentBefore.invoke(this)
        SwipeRefresh(
            state = refresState,
            onRefresh = onRefresh) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(itemList, key = { _, i -> i.getId() }) { idx, i ->
                    var itemOptionsDialog by remember { mutableStateOf(false) }
                    var editDialog by remember { mutableStateOf(false) }
                    var addDialog by remember { mutableStateOf(false) }
                    var moreDialog by remember { mutableStateOf(false) }
                    var deleteDialog by remember { mutableStateOf(false) }

                    SrmListItem(
                        modifier = if (idx == searchIdx)
                            Modifier.border(BorderStroke(2.dp, Color.Red))
                        else
                            Modifier,
                        startText = listItemStartText.invoke(i),
                        endText = listItemEndText.invoke(i),
                        icon = icon,
                        onClick = { itemOptionsDialog = true })

                    if (idx == searchIdx) {
                        LaunchedEffect(key1 = searchIdx, block = {
                            delay(delayDuration)
                            searchIdx = -1
                        })
                    }

                    if (itemOptionsDialog) {
                        crudDialogContent.apply {
                            SrmItemDialog(
                                editText = editLabel,
                                deleteText = deleteLabel,
                                moreText = moreLabel,
                                addText = addLabel,
                                onDismissRequest = { itemOptionsDialog = false },
                                onClickEdit = { editDialog = true },
                                onClickDelete = { deleteDialog = true },
                                onClickMore = { moreDialog = true },
                                onClickAdd = { addDialog = true },
                                enableAdd = addDialogContent != null,
                                enableEdit = editDialogContent != null,
                                enableMore = moreDialogContent != null,
                                enableDelete = onDelete != null,
                            )
                        }
                    }

                    crudDialogContent.apply {
                        this.addDialogContent?.let {
                            if (addDialog && userPermissions[1]) {
                                SrmDialog(onDismissRequest = { addDialog = false }) { it.invoke(this, i) }
                            }
                        }
                        editDialogContent?.let {
                            if (editDialog && userPermissions[2]) {
                                SrmDialog(onDismissRequest = { editDialog = false }) { it.invoke(this, i) }
                            }
                        }
                        moreDialogContent?.let {
                            if (moreDialog && userPermissions[3]) {
                                SrmDialog(onDismissRequest = { moreDialog = false }) { it.invoke(this, i) }
                            }
                        }
                        onDelete?.let {
                            if (deleteDialog && userPermissions[4]) {
                                SrmDeleteDialog(onDismissRequest = { deleteDialog = false }) {
                                    it.invoke(i)
                                }
                            }
                        }
                    }
                }
            }
        }
        contentAfter.invoke(this)
    }

    val scope = rememberCoroutineScope()
    if (dialogSearchRecipe) {
        searchProperties?.apply {
            SrmSearch(items = itemList,
                label = searchLabel,
                onDismissRequest = { dialogSearchRecipe = false },
                predicate = searchPredicate) { item ->
                SrmListItem(startText = startSearchText.invoke(item),
                    endText = endSearchText.invoke(item),
                    enableSelect = true,
                    onClick = {
                        onSearchItemClick.invoke(item)
                        scope.launch {
                            searchIdx = itemList.indexOfFirst {
                                it.getId() == item.getId()
                            }
                            lazyListState.scrollToItem(searchIdx)
                        }
                        dialogSearchRecipe = false
                    })
            }
        }
    }

    onAddDialog?.let {
        if (dialogAddState && userPermissions[0]) {
            SrmDialog(onDismissRequest = { dialogAddState = false }, content = it)
        }
    }
}