package com.srm.srmapp.ui.common

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, "Drop down icon", Modifier.rotate(if (expanded) 180f else 360f))
                if (!expanded)
                    focusManager.clearFocus()
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = Color.Transparent)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(onClick = {
                    onClick.invoke(selectionOption)
                    expanded = false
                }) {
                    itemView.invoke(selectionOption)
                }
            }
        }
    }
}