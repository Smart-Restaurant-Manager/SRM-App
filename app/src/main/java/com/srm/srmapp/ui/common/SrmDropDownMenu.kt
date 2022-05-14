package com.srm.srmapp.ui.common

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SrmDropDownMenu(
    text: String, options: List<T>,
    onClick: (T) -> Unit,
    itemView: @Composable (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
        expanded = !expanded
    }) {
        SrmTextField(modifier = Modifier.focusRequester(focusRequester),
            value = text, label = "", readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = Color.Transparent)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
            focusManager.clearFocus()
            expanded = false
        }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(onClick = {
                    focusManager.clearFocus()
                    onClick.invoke(selectionOption)
                    expanded = false
                }) {
                    itemView.invoke(selectionOption)
                }
            }
        }
    }
}