package com.srm.srmapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.srm.srmapp.ui.theme.DialogBackground
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart

@Composable
fun SrmDialog(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = paddingStart, end = paddingEnd)
            .background(color = DialogBackground,
                RoundedCornerShape(20)), content = content)
    }
}
