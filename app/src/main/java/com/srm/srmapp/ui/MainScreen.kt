package com.srm.srmapp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.destinations.BookingScreenDestination
import com.srm.srmapp.ui.destinations.FoodListScreenDestination
import com.srm.srmapp.ui.destinations.MenuScreenDestination
import com.srm.srmapp.ui.destinations.OrderScreenDestination
import com.srm.srmapp.ui.theme.ButtonColor1
import com.srm.srmapp.ui.theme.padding
import kotlin.system.exitProcess

@Composable
@com.ramcosta.composedestinations.annotation.Destination
fun ManagerScreen(navigator: DestinationsNavigator, userSession: UserSession) {
    val buttonNames = listOf(
        Pair(R.string.reservas) { navigator.navigate(BookingScreenDestination()) },
        Pair(R.string.food) { navigator.navigate(FoodListScreenDestination()) },
        Pair(R.string.menu) { navigator.navigate(MenuScreenDestination()) },
        Pair(R.string.Pedidos) { navigator.navigate(OrderScreenDestination()) },
        Pair(R.string.predictions) {})
    var popupState by remember { mutableStateOf(false) }
    val loggedIn by userSession.loggedIn.observeAsState(true)

    val poppinsFontFamily = FontFamily(
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    if (!loggedIn) {
        navigator.navigateUp()
    }
    if (popupState) {
        SrmDialog(onDismissRequest = { popupState = false }) {
            SrmSpacedRow(horizontalArrangement = Arrangement.SpaceEvenly) {
                SrmTextButton(textColor = Color.Red, onClick = { exitProcess(0) }, text = "Exit")
                SrmTextButton(onClick = {
                    userSession.logout()
                    popupState = false
                }, text = "Logout")
            }
        }
    }


    SrmHeader(title = stringResource(R.string.start)) { popupState = true }
    BackHandler { popupState = true }
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
