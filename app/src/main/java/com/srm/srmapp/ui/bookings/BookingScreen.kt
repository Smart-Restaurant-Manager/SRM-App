package com.srm.srmapp.ui.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
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
import com.srm.srmapp.ui.common.SrmAddTitleSearch
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.theme.ButtonColor2
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart


@Composable
@Destination
fun  BookingScreen ( navigator: DestinationsNavigator){

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

        Row(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()

           ){
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

}
