package com.srm.srmapp.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.srm.srmapp.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val padding = 8.dp
val paddingStart = 8.dp
val paddingEnd = 8.dp
val spacerWitdh = 20.dp
val textFieldPadding = 8.dp
val delayDuration: Duration = 750.milliseconds

val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)