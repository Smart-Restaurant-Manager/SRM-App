package com.srm.srmapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import timber.log.Timber

@Composable
fun <T> SrmSelectableRow(
    item: T,
    modifier: Modifier = Modifier,
    onClick: (T) -> Unit = {},
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit,
) {
    var selectedItem by remember { mutableStateOf<T?>(null) }
    Row(modifier = modifier
        .height(60.dp)
        .fillMaxWidth()
        .selectable(
            selected = selectedItem == item,
            onClick = {
                selectedItem = item
                Timber.d(item.toString())
                onClick.invoke(item)
            }
        ),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment, content = content)
}


@Composable
fun SrmSpacedRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit,
) {
    Row(modifier = modifier
        .height(60.dp)
        .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment, content = content)
}


@Composable
fun SrmSpacedColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.SpaceEvenly,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}