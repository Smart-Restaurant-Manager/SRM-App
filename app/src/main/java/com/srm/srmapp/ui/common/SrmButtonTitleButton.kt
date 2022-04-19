package com.srm.srmapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srm.srmapp.R
import com.srm.srmapp.ui.theme.ButtonColor2
import com.srm.srmapp.ui.theme.TextColor

@Composable
fun SrmButtonTitleButton(title: String, onClickLeft: () -> Unit = {}, onClickRight: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .height(30.dp)
            .fillMaxHeight()
    ) {
        IconButton(onClickLeft) {
            Icon(painter = painterResource(id = R.drawable.add), contentDescription = "Add")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            Modifier
                .fillMaxHeight()
                .border(1.dp, Color.White, RoundedCornerShape(20))
                .width(120.dp)
                .background(color = ButtonColor2, RoundedCornerShape(20))
        ) {
            SrmText(
                text = title,
                textAlign = TextAlign.Center,
                color = TextColor,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClickRight) {
            Icon(painter = painterResource(id = R.drawable.search), contentDescription = "Search")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewButtonTitleButton() {
    SrmButtonTitleButton("Preview")
}