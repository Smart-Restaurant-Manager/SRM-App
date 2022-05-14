package com.srm.srmapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SrmIconButton(modifier: Modifier = Modifier, width: Dp = 25.dp, height: Dp = 25.dp, painter: Painter, onClick: () -> Unit) {
    IconButton(modifier = modifier
        .clickable(onClick = onClick),
        onClick = onClick) {
        Icon(modifier = Modifier.size(width, height),
            painter = painter, contentDescription = null)
    }
}