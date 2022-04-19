package com.srm.srmapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val email = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val password2 = remember { mutableStateOf("") }
    val signUpState = viewmodel.getSignupState().observeAsState()
    val checkBox = remember { mutableStateOf(false) }

    SrmHeader(stringResource(id = R.string.new_account)) {
        navigator.popBackStack()
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SrmTextField(value = email.value,
            label = stringResource(id = R.string.email),
            enabled = signUpState.value !is Resource.Loading,
            isError = signUpState.value is Resource.Error,
            onValueChange = { email.value = it })

        SrmTextField(value = name.value,
            label = stringResource(id = R.string.name),
            enabled = signUpState.value !is Resource.Loading,
            isError = signUpState.value is Resource.Error,
            onValueChange = { name.value = it })

        SrmTextField(value = password.value,
            label = stringResource(id = R.string.password),
            enabled = signUpState.value !is Resource.Loading,
            isError = signUpState.value is Resource.Error,
            onValueChange = { password.value = it })

        SrmTextField(value = password2.value,
            label = stringResource(id = R.string.password_check),
            enabled = signUpState.value !is Resource.Loading,
            isError = signUpState.value is Resource.Error,
            onValueChange = { password2.value = it })

        SrmButton(onClick = {
            viewmodel.signup(email.value, name.value, password.value, password2.value)
        }, text = stringResource(id = R.string.register),
            enabled = signUpState.value !is Resource.Loading && checkBox.value)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = checkBox.value, onCheckedChange = { checkBox.value = it })
            Spacer(modifier = Modifier.size(16.dp))
            SrmText(text = stringResource(id = R.string.terms_conditions))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            SrmText(text = stringResource(id = R.string.login_account))
            TextButton({ navigator.popBackStack() }) {
                SrmText(text = stringResource(id = R.string.login_user))
            }
        }

        if (signUpState.value is Resource.Loading)
            CircularProgressIndicator()

        if (signUpState.value is Resource.Success) {
            SrmText(text = "Cuenta creada correctamente! Espere a ser rederigido.", textAlign = TextAlign.Center)
            LaunchedEffect(key1 = signUpState.value) {
                delay(4000)
                navigator.popBackStack()
            }
        }
    }
}