package com.srm.srmapp.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.srm.srmapp.ui.theme.ButtonColor1
import com.srm.srmapp.ui.theme.TextColor

@Composable
fun SrmButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor1),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
//    content: @Composable RowScope.() -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = { SrmText(text = text, color = TextColor) },
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewButton() {
    SrmButton(onClick = {}, text = "Preview Button")
}