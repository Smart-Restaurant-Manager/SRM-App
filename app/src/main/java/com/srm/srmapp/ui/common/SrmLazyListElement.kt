package com.srm.srmapp.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.srm.srmapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SrmCrudDialogContent<T>(
    // Dialog content, set to null to disable
    val editLabel: String = "Modificar",
    val editDialogContent: (@Composable (T) -> Unit)? = null,

    val deleteLabel: String = "Eliminar",
    val onDelete: ((T) -> Unit)? = null,

    val moreLabel: String = "Mes",
    val moreDialogContent: (@Composable (T) -> Unit)? = null,

    val addLabel: String = "Afegir",
    val addDialogContent: (@Composable (T) -> Unit)? = null,
)

data class SrmSearchProperties<T>(
    // Search parameters
    val searchPredicate: (T, String) -> Boolean,
    val indexPredicate: (T, T) -> Boolean,

    // Search Item view
    val searchLabel: String = "",
    val startSearchText: (T) -> String = { "" },
    val endSearchText: (T) -> String = { "" },

    // Search Item Click
    val onSearchItemClick: (T) -> Unit = { },
)

@Composable
fun <T> SrmListWithCrudActions(
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
    itemKey: ((item: T) -> Any)? = null,
    listItemStartText: (T) -> String = { "item" },
    listItemEndText: (T) -> String = { "" },

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
                itemsIndexed(itemList, key = { _, i -> itemKey?.invoke(i) ?: Unit }) { idx, i ->
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
                    ) { itemOptionsDialog = true }

                    if (idx == searchIdx) {
                        LaunchedEffect(key1 = searchIdx, block = {
                            delay(750)
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
                            if (addDialog) {
                                SrmDialog(onDismissRequest = { addDialog = false }) { it.invoke(i) }
                            }
                        }
                        editDialogContent?.let {
                            if (editDialog) {
                                SrmDialog(onDismissRequest = { editDialog = false }) { it.invoke(i) }
                            }
                        }
                        moreDialogContent?.let {
                            if (moreDialog) {
                                SrmDialog(onDismissRequest = { moreDialog = false }) { it.invoke(i) }
                            }
                        }
                        onDelete?.let {
                            if (deleteDialog) {
                                AlertDialog(onDismissRequest = { deleteDialog = false },
                                    confirmButton = {
                                        SrmTextButton(
                                            text = stringResource(R.string.affirmativeButton),
                                            onClick = {
                                                it.invoke(i)
                                                deleteDialog = false
                                            },
                                        )
                                    },
                                    dismissButton = {
                                        SrmTextButton(onClick = { deleteDialog = false },
                                            text = stringResource(R.string.negativeButton))
                                    },
                                    text = { SrmText(text = "Estas seguro?") })
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
                                indexPredicate.invoke(it, item)
                            }
                            lazyListState.scrollToItem(searchIdx)
                        }
                        dialogSearchRecipe = false
                    })
            }
        }
    }

    onAddDialog?.let {
        if (dialogAddState) {
            SrmDialog(onDismissRequest = { dialogAddState = false }, content = it)
        }
    }
}