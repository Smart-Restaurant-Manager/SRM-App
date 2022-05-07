package com.srm.srmapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.srm.srmapp.R
import com.srm.srmapp.ui.theme.ButtonColor2
import com.srm.srmapp.ui.theme.TextColor

@Composable
fun SrmAddTitleSearch(
    title: String,
    onClickAdd: () -> Unit = {},
    onClickSearch: () -> Unit = {},
    onClickBack: () -> Unit = {},
    showAdd: Boolean = true,
    showSearch: Boolean = true,
) {
    Box(Modifier
        .fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.SpaceAround) {
            IconButton(onClickBack, modifier = Modifier.padding(0.dp, 10.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Go Back",
                    modifier = Modifier.size(width = 25.dp, height = 25.dp),
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (showAdd) {
                    IconButton(onClickAdd) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_add_24),
                            contentDescription = "Add",
                            modifier = Modifier.size(25.dp, 25.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(25.dp, 25.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    Modifier
                        .width(150.dp)
                        .fillMaxHeight()
                        .background(color = ButtonColor2, RoundedCornerShape(20))
                ) {
                    SrmText(
                        text = title,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = TextColor,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (showSearch) {
                    IconButton(onClickSearch) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            modifier = Modifier.size(25.dp, 25.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(25.dp, 25.dp))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewButtonTitleButton() {
    SrmAddTitleSearch("Preview", showAdd = false, showSearch = true)
}