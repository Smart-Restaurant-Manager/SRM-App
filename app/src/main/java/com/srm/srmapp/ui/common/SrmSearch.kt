package com.srm.srmapp.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.srm.srmapp.ui.theme.padding
import timber.log.Timber


@Composable
fun <T> SrmSearch(
    items: List<T>,
    label: String,
    onDismissRequest: () -> Unit,
    predicate: (T, String) -> Boolean,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit,
) {
    var query: String by remember { mutableStateOf("") }
    val itemsFound = remember(key1 = query, key2 = items) {
        items.filter {
            Timber.d("Search query $query")
            if (query.isBlank()) return@filter false
            val res = predicate.invoke(it, query)
            res
        }
    }
    Timber.d("Items found $itemsFound")

    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmTextField(label = label, value = query, onValueChange = { query = it })
        LazyColumn(modifier = Modifier.heightIn(0.dp, 200.dp), contentPadding = PaddingValues(padding)) {
            items(itemsFound, itemContent = itemContent)
        }
    }
}
