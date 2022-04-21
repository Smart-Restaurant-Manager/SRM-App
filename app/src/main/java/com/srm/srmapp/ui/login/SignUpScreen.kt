package com.srm.srmapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.ui.common.SrmButton
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.common.SrmText
import com.srm.srmapp.ui.common.SrmTextField
import kotlinx.coroutines.delay


@Destination
@Composable
fun SignUpScreen(navigator: DestinationsNavigator, viewmodel: LoginViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    val signUpState by viewmodel.getSignupState().observeAsState()
    var checkBox by remember { mutableStateOf(false) }

    SrmHeader(stringResource(id = R.string.new_account)) { navigator.navigateUp() }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SrmTextField(value = email,
            label = stringResource(id = R.string.email),
            enabled = signUpState !is Resource.Loading,
            isError = signUpState is Resource.Error,
            onValueChange = { email = it })

        SrmTextField(value = name,
            label = stringResource(id = R.string.name),
            enabled = signUpState !is Resource.Loading,
            isError = signUpState is Resource.Error,
            onValueChange = { name = it })

        SrmTextField(value = password,
            label = stringResource(id = R.string.password),
            enabled = signUpState !is Resource.Loading,
            isError = signUpState is Resource.Error,
            onValueChange = { password = it })

        SrmTextField(value = password2,
            label = stringResource(id = R.string.password_check),
            enabled = signUpState !is Resource.Loading,
            isError = signUpState is Resource.Error,
            onValueChange = { password2 = it })

        SrmButton(onClick = {
            viewmodel.signup(email, name, password, password2)
        }, text = stringResource(id = R.string.register),
            enabled = signUpState !is Resource.Loading && checkBox)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = checkBox, onCheckedChange = { checkBox = it })
            Spacer(modifier = Modifier.size(16.dp))
            SrmText(text = stringResource(id = R.string.terms_conditions))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            SrmText(text = stringResource(id = R.string.login_account))
            TextButton({ navigator.popBackStack() }) {
                SrmText(text = stringResource(id = R.string.login_user))
            }
        }

        if (signUpState is Resource.Loading)
            CircularProgressIndicator()

        if (signUpState is Resource.Success) {
            SrmText(text = "Cuenta creada correctamente! Espere a ser rederigido.", textAlign = TextAlign.Center)
            LaunchedEffect(key1 = signUpState) {
                delay(4000)
                navigator.popBackStack()
            }
        }
    }
}