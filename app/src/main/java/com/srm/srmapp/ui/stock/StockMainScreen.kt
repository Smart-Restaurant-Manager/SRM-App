package com.srm.srmapp.ui.stock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.ui.common.SrmButton
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.theme.ButtonColor2

@Destination
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockMainScreen(navigator: DestinationsNavigator) {
    val buttonNames = listOf(
        R.string.carne,
        R.string.cereales,
        R.string.mariscos,
        R.string.vegetales,
        R.string.lacteos,
        R.string.especias,
    )
    SrmHeader(title = stringResource(id = R.string.title_stock)) { navigator.popBackStack() }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyVerticalGrid(cells = GridCells.Fixed(2)) {
            items(buttonNames) { buttonName ->
                SrmButton(modifier = Modifier
                    .padding(30.dp)
                    .width(150.dp)
                    .height(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor2),
                    onClick = { /*TODO*/ }, text = stringResource(id = buttonName))
            }
        }
    }
}