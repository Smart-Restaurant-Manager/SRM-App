package com.srm.srmapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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
import com.srm.srmapp.ui.theme.poppinsFontFamily


@Destination
@RootNavGraph(start = true)
@Composable
fun LoginForm(
    navigator: DestinationsNavigator,
    viewmodel: LoginViewModel = hiltViewModel(),
    userSession: UserSession,
) {
    var username by remember { mutableStateOf(userSession.getUser()) }
    var password by remember { mutableStateOf(userSession.getPassword()) }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    var saveUsernameAndPassword: Boolean by remember { mutableStateOf(false) }

    val user by userSession.user.observeAsState(Resource.Empty())
    val status by viewmodel.status.observeAsState(Resource.Empty())

    if (userSession.isSaved() && user.isEmpty() && status.isEmpty()) {
        viewmodel.login(userSession.getUser(), userSession.getPassword())
    }

    SrmHeader(stringResource(id = R.string.login2)) { navigator.navigateUp() }
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SrmTextField(
            value = username,
            enabled = !user.isLoading() || !status.isLoading(),
            label = stringResource(id = R.string.user_mail),
            onValueChange = { username = it },
        )

        SrmTextField(
            value = password,
            label = stringResource(id = R.string.password),
            enabled = !user.isLoading() || !status.isLoading(),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisibility) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Image(image, contentDescription = description)
                }
            },
            onValueChange = { password = it },
            modifier = Modifier
                .padding(0.dp, 20.dp),
        )

        SrmCheckBox(
            text = "Guardar usuario",
            modifier = Modifier
                .padding(30.dp, 0.dp),
        ) { saveUsernameAndPassword = it }

        SrmButton(
            onClick = { viewmodel.login(username, password) },
            text = stringResource(id = R.string.login),
            enabled = !status.isLoading() && !user.isLoading(),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            SrmText(text = stringResource(id = R.string.no_account), fontFamily = poppinsFontFamily, fontWeight = FontWeight.Normal)
            TextButton({ navigator.navigate(SignUpScreenDestination()) }) {
                SrmText(text = stringResource(id = R.string.register), maxLines = 2, fontFamily = poppinsFontFamily, fontWeight = FontWeight.Bold)
            }
        }

        if (status.isSuccess()) {
            status.data?.let {
                SrmText(text = it)
            }
        }

        if (user.isLoading() || status.isLoading()) {
            CircularProgressIndicator()
        }

        if (user.isSuccess()) {
            if (saveUsernameAndPassword)
                userSession.setUsernameAndPassword(username, password)
            viewmodel.clearStatus()
            navigator.navigate(ManagerScreenDestination())
        }
    }
}