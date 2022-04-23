package com.srm.srmapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
        SrmSpacedColumn(Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = paddingStart, end = paddingEnd)
            .background(color = DialogBackground,
                RoundedCornerShape(5)), content = content)
    }
}
