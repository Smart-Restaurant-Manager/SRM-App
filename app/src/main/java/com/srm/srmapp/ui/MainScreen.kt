package com.srm.srmapp.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.destinations.*
import com.srm.srmapp.ui.theme.ButtonColor1
import kotlin.system.exitProcess

@Composable
@com.ramcosta.composedestinations.annotation.Destination
fun ManagerScreen(navigator: DestinationsNavigator, userSession: UserSession) {
    val buttonNames = remember {
        val l = mutableListOf(
            Pair(R.string.reservas) { navigator.navigate(BookingScreenDestination()) },
            Pair(R.string.food) { navigator.navigate(FoodListScreenDestination()) },
            Pair(R.string.menu) { navigator.navigate(MenuScreenDestination()) },
            Pair(R.string.pedidos) { navigator.navigate(OrderScreenDestination()) },
        )

        if (userSession.getRole() == 0)
            l.add(Pair(R.string.predictions) { navigator.navigate(PredictionScreenDestination()) })
        l.toList()
    }

    var popupState by remember { mutableStateOf(false) }

    if (popupState) {
        SrmDialog(onDismissRequest = { popupState = false }) {
            SrmSpacedRow(horizontalArrangement = Arrangement.SpaceEvenly) {
                SrmTextButton(textColor = Color.Red, onClick = { exitProcess(0) }, text = "Exit")
                SrmTextButton(onClick = {
                    userSession.clearUsernameAndPassowrd()
                    userSession.logout()
                    navigator.navigateUp()
                    popupState = false
                }, text = "Logout")
            }
        }
    }

    BackHandler { popupState = true }
    Column(modifier = Modifier
        .fillMaxSize()) {
        SrmHeader(title = stringResource(R.string.start)) { popupState = true }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, Alignment.CenterHorizontally) {
            buttonNames.forEach { (id, onclick) ->
                SrmButton(modifier = Modifier
                    .width(150.dp)
                    .height(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor1),
                    onClick = onclick,
                    text = stringResource(id = id))
            }
            Socials()
        }
    }
}

@Composable
fun Socials() {
    val context = LocalContext.current
    val intentWeb = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://smart-restaurant-manager.herokuapp.com/")) }
    val intentInsta = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/")) }
    val intentTripadvisor = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tripadvisor.es/")) }

    Row {
        OutlinedButton(
            onClick = { context.startActivity(intentWeb) },
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(1.dp, Color.Black, CircleShape)
                    .size(30.dp, 30.dp)
            )
        }
        OutlinedButton(
            onClick = { context.startActivity(intentInsta) },
        ) {
            Image(
                painter = painterResource(id = R.drawable.instagram),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(1.dp, Color.Black, CircleShape)
                    .size(30.dp, 30.dp)
            )
        }
        OutlinedButton(
            onClick = { context.startActivity(intentTripadvisor) },
            // shape = CircleShape, // = 50% percent
            // or shape = CircleShape
        ) {
            Image(
                painter = painterResource(id = R.drawable.tripadvisor),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(1.dp, Color.Black, CircleShape)
                    .size(30.dp, 30.dp)
            )
        }
    }
}