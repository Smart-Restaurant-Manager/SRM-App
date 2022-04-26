package com.srm.srmapp.ui.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.stock.StockViewmodel
import com.srm.srmapp.ui.theme.ButtonColor2
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart


@Composable
@Destination
fun  BookingScreen (
    navigator: DestinationsNavigator,
){

    val array = listOf<String>("Nombre","Personas","Fecha")
    var popupAddState by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(title = stringResource(id = R.string.reservas),
        onClickSearch = {},
            onClickAdd = {popupAddState = true},
            onClickBack = {navigator.navigateUp()}
        )
        Spacer(modifier = Modifier.width(20.dp))

        Row(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically){
            for(j in 0 until 3) {
                    Box(
                        modifier = Modifier
                            .background(color = ButtonColor2, RoundedCornerShape(20))
                            .size(120.dp)
                            .fillMaxHeight()



                    ) {
                        Text(text=array[j],color = Color.White, modifier = Modifier.align(Alignment.Center)
                       )
                    }
                  Spacer(modifier = Modifier.width(5.dp))

            }


        }
    }

    if (popupAddState) {
        var name by remember { mutableStateOf("") }
        var amount_people by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("")}
        var telephone by remember { mutableStateOf("")}
        var mail by remember { mutableStateOf("")}
        var table by remember { mutableStateOf("")}
        SrmDialog(onDismissRequest = {
            popupAddState = false
        }) {
            SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
            SrmTextFieldHint(value = amount_people, placeholder = stringResource(R.string.amount_of_people), onValueChange = { amount_people = it })
            SrmTextFieldHint(value = date, placeholder = stringResource(R.string.date), onValueChange = { date = it })
            SrmTextFieldHint(value = telephone, placeholder = stringResource(R.string.tel), onValueChange = { telephone = it })
            SrmTextFieldHint(value = mail, placeholder = stringResource(R.string.mail), onValueChange = { mail = it })
            SrmTextFieldHint(value = table, placeholder = stringResource(R.string.table), onValueChange = { table = it })

            TextButton(onClick = {
                //viewmodel.addBooking(type, name, units)
                popupAddState = false
            }) {
                SrmText(text = stringResource(R.string.add_booking))
            }
        }
    }

}
