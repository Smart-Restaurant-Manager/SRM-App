package com.srm.srmapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.srm.srmapp.R
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart

@Composable
fun SrmListItem(
    modifier: Modifier = Modifier,
    startText: String,
    endText: String = "",
    icon: Painter? = null,
    enableSelect: Boolean = true,
    onClick: () -> Unit = {},
) {
    ConstraintLayout(modifier = modifier
        .height(60.dp)
        .fillMaxWidth()
        .padding(start = paddingStart, end = paddingEnd)
        .clickable(enabled = enableSelect, onClick = onClick)) {
        val (iconRef, textStartRef, textEndRef, bottomLineRef) = createRefs()
        if (icon != null) {
            Icon(modifier = Modifier
                .constrainAs(iconRef) {
                    start.linkTo(parent.start)
                    end.linkTo(textStartRef.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomLineRef.top)
                }
                .fillMaxWidth(.2f)
                .fillMaxHeight(),
                painter = icon,
                contentDescription = "")
        }
        SrmText(text = startText,
            modifier = Modifier
                .constrainAs(textStartRef) {
                    if (icon != null)
                        start.linkTo(iconRef.end)
                    else
                        start.linkTo(parent.start)
                    end.linkTo(textEndRef.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomLineRef.top)
                }
                .fillMaxWidth(if (icon != null) .5f else 0.7f)
        )
        SrmText(text = endText,
            modifier = Modifier
                .constrainAs(textEndRef) {
                    start.linkTo(textStartRef.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomLineRef.top)
                }
                .fillMaxWidth(.3f), textAlign = TextAlign.End, color = Color.LightGray)
        Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Color.DarkGray)
            .constrainAs(bottomLineRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
    }
}


@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    SrmListItem(startText = "Main\nSub", endText = "End1\nEnd2", icon = painterResource(id = R.drawable.ic_baseline_image_not_supported_24)) {

    }
}