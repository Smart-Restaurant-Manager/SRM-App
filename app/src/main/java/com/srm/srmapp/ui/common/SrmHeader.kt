package com.srm.srmapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.srm.srmapp.R

@Composable
fun SrmHeader(title: String, onClickLeft: () -> Unit = {}) {
    ConstraintLayout(
        modifier = Modifier
            .padding(top = 40.dp, bottom = 40.dp)
            .height(70.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
    ) {
        val (icon, text) = createRefs()
        IconButton(onClickLeft, modifier = Modifier
            .constrainAs(icon) {
                end.linkTo(text.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            .padding(end = 20.dp)
            .wrapContentSize()) {
            Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Go Back")
        }
        SrmText(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            softWrap = true,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(text) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .wrapContentSize()
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewHeader() {
    SrmHeader("Preview Title")
}