package com.srm.srmapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.ui.common.SrmButton
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.destinations.FoodListScreenDestination
import com.srm.srmapp.ui.theme.ButtonColor1
import com.srm.srmapp.ui.theme.padding

@Composable
@com.ramcosta.composedestinations.annotation.Destination
fun ManagerScreen(navigator: DestinationsNavigator) {
    val buttonNames = listOf(
        Pair(R.string.reservas) {},
        Pair(R.string.food) { navigator.navigate(FoodListScreenDestination()) },
        Pair(R.string.menu) {},
        Pair(R.string.predictions) {})
    SrmHeader(title = stringResource(R.string.start)) { navigator.navigateUp() }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyColumn {
            items(buttonNames) { (id, onclick) ->
                SrmButton(modifier = Modifier
                    .padding(padding)
                    .width(150.dp)
                    .height(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor1),
                    onClick = onclick,
                    text = stringResource(id = id))
            }
        }
    }
}
