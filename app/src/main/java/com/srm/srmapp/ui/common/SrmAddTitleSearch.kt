package com.srm.srmapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import com.srm.srmapp.ui.theme.poppinsFontFamily

@Composable
fun SrmAddTitleSearch(
    title: String,
    onClickAdd: () -> Unit = {},
    onClickSearch: () -> Unit = {},
    onClickBack: () -> Unit = {},
    enableAdd: Boolean = true,
    enableSearch: Boolean = true,
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .height(125.dp)) {
        val (iconRef, addRef, titleRef, searchRef) = createRefs()
        IconButton(modifier = Modifier
            .constrainAs(iconRef) {
                centerHorizontallyTo(parent, 1 / 16f)
                centerVerticallyTo(parent, 0.25f)
            }, onClick = onClickBack) {
            Icon(modifier = Modifier
                .size(25.dp, 25.dp),
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "Go Back")
        }

        if (enableAdd) {
            IconButton(modifier = Modifier
                .clickable(onClick = onClickAdd)
                .constrainAs(addRef) {
                    centerHorizontallyTo(parent, 1 / 8f)
                    centerVerticallyTo(parent, 0.75f)
                }, onClick = onClickAdd) {

                Icon(modifier = Modifier
                    .size(25.dp, 25.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = "Add")
            }
        }
        SrmText(
            modifier = Modifier.constrainAs(titleRef) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent, 0.75f)
            },
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            softWrap = true,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
        )

        if (enableSearch) {
            IconButton(modifier = Modifier
                .clickable(onClick = onClickSearch)
                .constrainAs(searchRef) {
                    centerHorizontallyTo(parent, 1 - 1 / 8f)
                    centerVerticallyTo(parent, 0.75f)
                }, onClick = onClickSearch) {
                Icon(modifier = Modifier
                    .size(25.dp, 25.dp),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewButtonTitleButton() {
    SrmAddTitleSearch("Preview", enableAdd = true, enableSearch = true)
}