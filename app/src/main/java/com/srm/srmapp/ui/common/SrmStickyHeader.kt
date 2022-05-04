package com.srm.srmapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.srm.srmapp.ui.theme.ButtonColor2

@Composable
fun SrmStickyHeader(headers: List<String>) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (text in headers) {
            Box(
                modifier = Modifier
                    .background(color = ButtonColor2, RoundedCornerShape(20))
                    .size(120.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}