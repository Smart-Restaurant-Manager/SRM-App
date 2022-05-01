package com.srm.srmapp.ui.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.ui.common.SrmAddTitleSearch
import com.srm.srmapp.ui.common.SrmDialog
import com.srm.srmapp.ui.common.SrmText
import com.srm.srmapp.ui.common.SrmTextFieldHint
import com.srm.srmapp.ui.theme.ButtonColor2
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart
import timber.log.Timber


@Composable
@Destination
fun BookingScreen(
    navigator: DestinationsNavigator,
    viewmodel: BookingViewModel = hiltViewModel(),
) {

    val array = listOf("Nombre", "Personas", "Fecha")
    var popupAddState by remember { mutableStateOf(false) }
    val status by viewmodel.status.observeAsState(Resource.Empty())

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally) {
        SrmAddTitleSearch(title = stringResource(id = R.string.reservas),
            onClickSearch = {},
            onClickAdd = { popupAddState = true },
            onClickBack = { navigator.navigateUp() }
        )
        Spacer(modifier = Modifier.width(20.dp))

        Row(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            for (j in 0 until 3) {
                Box(
                    modifier = Modifier
                        .background(color = ButtonColor2, RoundedCornerShape(20))
                        .size(120.dp)
                        .fillMaxHeight()


                ) {
                    Text(text = array[j], color = Color.White, modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))

            }
        }
    }

    if (status.isSuccess()) {
        Timber.d("got status $status")
    }

    if (popupAddState) {
        var name by remember { mutableStateOf("") }
        var amountPeople by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var telephone by remember { mutableStateOf("") }
        var mail by remember { mutableStateOf("") }
        var table by remember { mutableStateOf("") }
        SrmDialog(onDismissRequest = {
            popupAddState = false
        }) {
            SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
            SrmTextFieldHint(value = amountPeople, placeholder = stringResource(R.string.amount_of_people), onValueChange = { amountPeople = it })
            SrmTextFieldHint(value = date, placeholder = stringResource(R.string.date), onValueChange = { date = it })
            SrmTextFieldHint(value = telephone, placeholder = stringResource(R.string.tel), onValueChange = { telephone = it })
            SrmTextFieldHint(value = mail, placeholder = stringResource(R.string.mail), onValueChange = { mail = it })
            SrmTextFieldHint(value = table, placeholder = stringResource(R.string.table), onValueChange = { table = it })

            TextButton(onClick = {
                val people = try {
                    amountPeople.toInt()
                } catch (e: NumberFormatException) {
                    return@TextButton
                }
                viewmodel.addBooking(name, people, date, telephone, mail, table)
                popupAddState = false
            }) {
                SrmText(text = stringResource(R.string.add_booking))
            }
        }
    }

}
