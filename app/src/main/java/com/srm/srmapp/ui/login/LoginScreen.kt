package com.srm.srmapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.ui.common.SrmButton
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.common.SrmText
import com.srm.srmapp.ui.common.SrmTextField
import com.srm.srmapp.ui.destinations.SignUpScreenDestination
import com.srm.srmapp.ui.destinations.StockMainScreenDestination

@Destination
@RootNavGraph(start = true)
@Composable
fun LoginScreen(navigator: DestinationsNavigator, viewmodel: LoginViewModel = hiltViewModel()) {
    val user = remember { mutableStateOf("q@q") }
    val password = remember { mutableStateOf("q") }
    val loginState = viewmodel.getLoginState().observeAsState()


    SrmHeader(stringResource(id = R.string.login2)) {
        navigator.popBackStack()
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SrmTextField(value = user.value,
            label = stringResource(id = R.string.user_mail),
            enabled = loginState.value !is Resource.Loading,
            isError = loginState.value is Resource.Error,
            onValueChange = { user.value = it })

        SrmTextField(value = password.value,
            label = stringResource(id = R.string.password),
            enabled = loginState.value !is Resource.Loading,
            isError = loginState.value is Resource.Error,
            onValueChange = { password.value = it })

        SrmButton(onClick = {
            viewmodel.login(user.value, password.value)
        }, text = stringResource(id = R.string.login),
            enabled = loginState.value !is Resource.Loading)

        Row(verticalAlignment = Alignment.CenterVertically) {
            SrmText(text = stringResource(id = R.string.no_account))
            TextButton({ navigator.navigate(SignUpScreenDestination()) }) {
                SrmText(text = stringResource(id = R.string.register), maxLines = 2)
            }
        }

        if (loginState.value is Resource.Loading)
            CircularProgressIndicator()

        if (loginState.value is Resource.Success)
            navigator.navigate(StockMainScreenDestination())
    }
}