package com.srm.srmapp.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.srm.srmapp.ui.destinations.*
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
        Pair(R.string.predictions) { navigator.navigate(PredictionScreenDestination())})
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

    val context = LocalContext.current
    val intent_web = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://smart-restaurant-manager.herokuapp.com/")) }
    val intent_insta = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/")) }
    val intent_tripadvisor = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tripadvisor.es/")) }
    SrmHeader(title = stringResource(R.string.start)) { popupState = true }
    BackHandler { popupState = true }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp, bottom = 20.dp), contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(buttonNames) { (id, onclick) ->
                SrmButton(
                    modifier = Modifier
                        .padding(padding)
                        .width(150.dp)
                        .height(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor1),
                    onClick = onclick,
                    text = stringResource(id = id)
                )

            }


        }
        Row(modifier = Modifier.padding(top = 630.dp)) {
            OutlinedButton(
                onClick = { context.startActivity(intent_web) },

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
                onClick = { context.startActivity(intent_insta) },
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

                onClick = { context.startActivity(intent_tripadvisor) },
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


}

