package com.srm.srmapp.ui.predictions

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.data.dto.predictions.PredictionObject
import com.srm.srmapp.ui.common.SrmButton
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.common.SrmText
import com.srm.srmapp.ui.common.SrmTextField


@Composable
@Destination
fun PredictionScreen(
    navigator: DestinationsNavigator,
    viewmodel: PredictionsViewModel,
){
    val poppinsFontFamily = FontFamily(
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )
    SrmHeader(title = stringResource(R.string.predictions)) { navigator.navigateUp() }
    var date by remember { mutableStateOf("08/07/2022") }
    var festivo_o_no by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ){
        SrmTextField(
            value = date,
            label = stringResource(id = R.string.date),
            onValueChange = { date = it },
            modifier = Modifier.padding(0.dp, 0.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp, 10.dp)
        ) {
            SrmText(text = stringResource(id = R.string.festivo_o_no), fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal)
            Checkbox(
                checked = festivo_o_no, onCheckedChange = { festivo_o_no = it },
                modifier = Modifier.padding(20.dp)
            )


        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp, 10.dp)
        ) {
            SrmButton(
                onClick = {
                    val prediction = PredictionObject(date = date, festive = festivo_o_no)
                    viewmodel.postPrediction(prediction)
                },
                text = stringResource(id = R.string.send),
            )
            var enabled  by rememberSaveable { mutableStateOf(true) }
            val context = LocalContext.current
            SrmButton(
                onClick = {
                    enabled = false
                    Toast.makeText(context, "Reentrenando el modelo", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        enabled = true
                        Toast.makeText(context, "Modelo reentrenado", Toast.LENGTH_SHORT).show()
                    }, 300000)
                },
                text = stringResource(id = R.string.retraining),
                modifier = Modifier.padding(30.dp,0.dp),
                enabled = enabled
            )

        }
    }
}
