package com.srm.srmapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.srm.srmapp.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay


@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator,
    viewmodel: LoginViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var checkBox by remember { mutableStateOf(false) }
    val state by viewmodel.status.observeAsState(Resource.Empty())

    SrmHeader(stringResource(id = R.string.new_account)) { navigator.navigateUp() }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SrmTextField(value = email,
            label = stringResource(id = R.string.email),
            enabled = state !is Resource.Loading,
            isError = state is Resource.Error,
            onValueChange = { email = it }
        )

        SrmTextField(
            value = name,
            label = stringResource(id = R.string.name),
            enabled = state !is Resource.Loading,
            isError = state is Resource.Error,
            onValueChange = { name = it },
        )
        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        SrmTextField(value = password,
            label = stringResource(id = R.string.password),
            enabled = state !is Resource.Loading,
            isError = state is Resource.Error,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisibility) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Image(image, contentDescription = description)
                }
            },
            onValueChange = { password = it }
        )

        SrmTextField(
            value = password2,
            label = stringResource(id = R.string.password_check),
            enabled = state !is Resource.Loading,
            isError = state is Resource.Error,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisibility) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Image(image, contentDescription = description)
                }
            },
            onValueChange = { password2 = it },
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = checkBox, onCheckedChange = { checkBox = it },
            )
            Spacer(modifier = Modifier.size(16.dp))
            SrmText(text = stringResource(id = R.string.terms_conditions), fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal)
        }

        SrmButton(
            onClick = {
                viewmodel.signup(email, name, password, password2)
            },
            text = stringResource(id = R.string.register),
            enabled = state !is Resource.Loading && checkBox,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            SrmText(text = stringResource(id = R.string.login_account), fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal)
            TextButton({ navigator.popBackStack() }) {
                SrmText(text = stringResource(id = R.string.login_user), fontFamily = poppinsFontFamily, fontWeight = FontWeight.Bold)
            }
        }

        if (state is Resource.Loading)
            CircularProgressIndicator()

        if (state is Resource.Success) {
            SrmText(text = "Cuenta creada correctamente! Espere a ser rederigido.",
                textAlign = TextAlign.Center,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal)
            LaunchedEffect(key1 = state) {
                delay(4000)
                navigator.popBackStack()
            }
        }
        if (state is Resource.Error) {
            SrmText(text = "Cuenta no creada. Revise los datos introducidos",
                textAlign = TextAlign.Center,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal)
        }
    }
}