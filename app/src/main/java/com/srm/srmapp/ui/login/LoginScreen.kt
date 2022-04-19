package com.srm.srmapp.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(navigator: DestinationsNavigator) {
    Column {
        TextField(value = "User", onValueChange = {})
        TextField(value = "Password", onValueChange = {})
        Button(onClick = { navigator.popBackStack() }) {
        }
    }
}