package com.srm.srmapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SrmRadioSelector(
    title: String,
    optionsList: List<String>,
    onClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedOption by remember { mutableStateOf(0) }
    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Surface(modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp)) {
            Column(modifier = Modifier.padding(10.dp)) {
                SrmText(text = title)
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(modifier = Modifier.width(500.dp)) {
                    itemsIndexed(optionsList) { idx, item ->
                        SrmSelectableRow(onClick = {
                            selectedOption = idx
                            onClick.invoke(idx)
                        }) {
                            Checkbox(checked = selectedOption == idx, onCheckedChange = {})
                            SrmText(text = item)
                        }
                    }
                }
            }
        }
    }
}