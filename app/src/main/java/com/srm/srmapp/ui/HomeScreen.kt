package com.srm.srmapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.ui.destinations.LoginScreenDestination

@Composable
@RootNavGraph(start = true)
@Destination
fun HomeScreen(navigator: DestinationsNavigator) {
    Column() {
        Text(text = "Hello!")
        Button(onClick = { navigator.navigate(LoginScreenDestination()) }) {
        }
    }
}
