package com.srm.srmapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SrmSpacedRowItem(item: T, content: @Composable RowScope.() -> Unit) {
    var selectedItem by remember { mutableStateOf<T?>(null) }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)) {
        Row(modifier = Modifier
            .fillMaxSize()
            .selectable(
                selected = selectedItem == item,
                onClick = { selectedItem = item }
            ),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically, content = content)
    }
}