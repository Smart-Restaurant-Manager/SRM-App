package com.srm.srmapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart

@Composable
fun SrmCheckBox(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    checkState: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    var checked by remember { mutableStateOf(checkState) }
    ConstraintLayout(modifier = modifier
        .height(60.dp)
        .fillMaxWidth()
        .padding(start = paddingStart, end = paddingEnd)
    ) {
        val (a, b) = createRefs()
        Checkbox(
            modifier = Modifier
                .constrainAs(a) {
                    start.linkTo(parent.start)
                    end.linkTo(b.start)
                    centerVerticallyTo(parent)
                }
                .fillMaxWidth(0.2f),
            checked = checked,
            enabled = enabled,
            onCheckedChange = {
                checked = it
                onCheckedChange.invoke(checked)
            })
        SrmText(text = text,
            modifier = Modifier
                .constrainAs(b) {
                    start.linkTo(a.end)
                    end.linkTo(parent.end)
                    centerVerticallyTo(parent)
                }
                .clickable(enabled = enabled, onClick = {
                    checked = !checked
                    onCheckedChange.invoke(checked)
                })
                .fillMaxWidth(0.8f))
    }
}

@Preview(showBackground = true)
@Composable
fun SrmCheckBoxPreview() {
    SrmCheckBox(text = "Test")
}