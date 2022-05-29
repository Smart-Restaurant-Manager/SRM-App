package com.srm.srmapp.ui.predictions

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.ui.common.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.minutes


@Composable
@Destination
fun PredictionScreen(
    navigator: DestinationsNavigator,
    viewmodel: PredictionsViewModel,
) {
    val poppinsFontFamily = FontFamily(
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )
    SrmHeader(title = stringResource(R.string.predictions)) { navigator.navigateUp() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var date by remember { mutableStateOf(LocalDateTime.now()) }
        var isFestivo by remember { mutableStateOf(false) }
        var enabled by rememberSaveable { mutableStateOf(true) }
        val scope = rememberCoroutineScope()
        SrmCheckBox(text = stringResource(id = R.string.festivo_o_no), onCheckedChange = {
            isFestivo = it
        })

        SrmDateTimeEditor(value = date, label = "Dia", onValueChange = {
            date = it
        }, onErrorAction = { })

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            SrmButton(
                onClick = {
                    viewmodel.postPrediction(date, isFestivo)
                },
                text = stringResource(id = R.string.send),
            )

            SrmButton(
                onClick = {
                    enabled = false
                    scope.launch {
                        delay(5.minutes)
                        enabled = true
                    }
                },
                text = stringResource(id = R.string.retraining),
                enabled = enabled
            )

        }

        if (!enabled)
            SrmText(text = "Reentrenado")
    }
}
