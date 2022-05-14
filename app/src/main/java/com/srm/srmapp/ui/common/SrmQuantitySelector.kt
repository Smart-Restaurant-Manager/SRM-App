package com.srm.srmapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.srm.srmapp.R
import com.srm.srmapp.Utils.format

@Composable
fun SrmQuantitySelector(
    optionsList: List<String>,
    selectorState: Array<Pair<Int, Float>> = arrayOf(),
    onUpdate: (Map<Int, Float>) -> Unit,
) {
    val quantityMap = remember { mutableStateMapOf(*selectorState) }
    LazyColumn(modifier = Modifier
        .heightIn(0.dp, 300.dp)
        .fillMaxWidth()) {
        itemsIndexed(optionsList) { idx, item ->
            ConstraintLayout(modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()) {
                val (leftReft, midRef, righRef, righRef2) = createRefs()
                Icon(modifier = Modifier
                    .clickable {
                        quantityMap[idx] = quantityMap.getOrDefault(idx, 0f) - 1
                        if (quantityMap[idx] ?: 0f <= 0f)
                            quantityMap.remove(idx)
                        onUpdate.invoke(quantityMap)
                    }
                    .constrainAs(leftReft) {
                        start.linkTo(parent.start)
                        end.linkTo(midRef.start)
                        centerVerticallyTo(parent)
                    }
                    .fillMaxHeight()
                    .fillMaxWidth(0.15f),
                    painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                    contentDescription = "Substract")
                Box(modifier = Modifier
                    .constrainAs(midRef) {
                        start.linkTo(leftReft.end)
                        end.linkTo(righRef.start)
                        centerVerticallyTo(parent)
                    }
                    .fillMaxHeight()
                    .fillMaxWidth(0.15f), contentAlignment = Alignment.Center) {
                    SrmText(text = "${quantityMap[idx]?.format(2) ?: 0}")
                }
                Icon(modifier = Modifier
                    .clickable {
                        quantityMap[idx] = quantityMap.getOrDefault(idx, 0f) + 1
                        if (quantityMap[idx] == 0f)
                            quantityMap.remove(idx)
                        onUpdate.invoke(quantityMap)
                    }
                    .constrainAs(righRef) {
                        start.linkTo(midRef.end)
                        end.linkTo(righRef2.start)
                        centerVerticallyTo(parent)
                    }
                    .fillMaxHeight()
                    .fillMaxWidth(0.15f),
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = "Add")
                Box(modifier = Modifier
                    .constrainAs(righRef2) {
                        start.linkTo(righRef2.start)
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    }
                    .fillMaxHeight()
                    .fillMaxWidth(0.45f),
                    contentAlignment = Alignment.Center) {
                    SrmText(text = item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuantity() {
    SrmQuantitySelector(optionsList = (1..20).toList().map { it.toString() }, selectorState = arrayOf(Pair(0, 2f)), onUpdate = {})

}