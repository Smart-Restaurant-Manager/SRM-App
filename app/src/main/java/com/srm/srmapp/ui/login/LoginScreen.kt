package com.srm.srmapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.destinations.ManagerScreenDestination
import com.srm.srmapp.ui.destinations.SignUpScreenDestination

@Destination
@RootNavGraph(start = true)
@Composable
fun LoginScreen(navigator: DestinationsNavigator, viewmodel: LoginViewModel = hiltViewModel(), userSession: UserSession) {

    val userState by userSession.userObject.observeAsState(Resource.Empty())
    if (userSession.isLoggedIn()) {
        when {
            userState.isEmpty() -> userSession.refresUser()
            userState.isLoading() -> {
                SrmSpacedColumn(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                    SrmText(text = "Loading ...")
                }
            }
            userState.isSuccess() -> navigator.navigate(ManagerScreenDestination())
        }
    } else {
        userSession.logout()
        LoginForm(navigator = navigator, viewmodel = viewmodel, userSession = userSession)
    }
}


@Composable
fun LoginForm(navigator: DestinationsNavigator, viewmodel: LoginViewModel, userSession: UserSession) {
    var user by remember { mutableStateOf("q@q") }
    var password by remember { mutableStateOf("q") }
    val loginState by viewmodel.loginState.observeAsState(Resource.Empty())
    val loggedIn by userSession.loggedIn.observeAsState(false)

    SrmHeader(stringResource(id = R.string.login2)) { navigator.navigateUp() }
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SrmTextField(value = user,
            label = stringResource(id = R.string.user_mail),
            enabled = loginState !is Resource.Loading,
            isError = loginState is Resource.Error,
            onValueChange = { user = it })

        SrmTextField(value = password,
            label = stringResource(id = R.string.password),
            enabled = loginState !is Resource.Loading,
            isError = loginState is Resource.Error,
            onValueChange = { password = it })

        SrmButton(onClick = {
            viewmodel.login(user, password)
        }, text = stringResource(id = R.string.login),
            enabled = loginState !is Resource.Loading)

        Row(verticalAlignment = Alignment.CenterVertically) {
            SrmText(text = stringResource(id = R.string.no_account))
            TextButton({ navigator.navigate(SignUpScreenDestination()) }) {
                SrmText(text = stringResource(id = R.string.register), maxLines = 2)
            }
        }

        if (loginState is Resource.Loading)
            CircularProgressIndicator()

        if (loggedIn) {
            viewmodel.clearLoginStatus()
            navigator.navigate(ManagerScreenDestination())
        }
    }
}