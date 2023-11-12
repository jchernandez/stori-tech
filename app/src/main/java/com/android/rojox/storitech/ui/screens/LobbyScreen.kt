package com.android.rojox.storitech.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.rojox.core.CoreModule
import com.android.rojox.core.model.DataStatus
import com.android.rojox.storitech.ui.UiApp
import com.android.rojox.storitech.ui.navigation.NavigationActions
import com.android.rojox.storitech.R
import com.android.rojox.storitech.ui.components.ProgressDialog
import com.android.rojox.storitech.ui.components.SecureTextField
import com.android.rojox.storitech.ui.components.TextField
import com.android.rojox.storitech.ui.navigation.ActionType
import com.android.rojox.storitech.utils.mockDbClient
import com.android.rojox.storitech.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
fun LobbyScreen(
    viewModel: LoginViewModel,
    actions: NavigationActions = NavigationActions()
) {

    val userSession by viewModel.userSession.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val email: String by viewModel.email.collectAsState()
    val isLoginEnabled by viewModel.isLoginEnabled.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    UiApp {
        when(userSession?.status) {
            DataStatus.LOADING -> ProgressDialog()
            DataStatus.ERROR -> scope.launch {
                snackBarHostState
                    .showSnackbar(
                        message = context.getString(R.string.error_login),
                        duration = SnackbarDuration.Short
                    )
            }
            DataStatus.SUCCESS -> {
                actions.onAction(ActionType.CLICK_LOGIN)
            }
            else -> {}
        }


        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = Dp(200f)),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo image",
                )

                Column {

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = email,
                        placeholder = stringResource(id = R.string.email),
                        onValueChange = { viewModel.email.value = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    SecureTextField(
                        modifier = Modifier.fillMaxWidth(),
                        password = viewModel.password,
                        placeholder = stringResource(id = R.string.password),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            })
                    )
                    Text(
                        text = stringResource(id = R.string.password_details),
                        style = MaterialTheme.typography.caption)

                }

                Column(
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Button(
                        onClick = { viewModel.login() },
                        modifier =
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        enabled = isLoginEnabled
                    ) {
                        Text(text = stringResource(id = R.string.login))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { actions.onAction(ActionType.CLICK_SIGN_IN) },
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.sign_in))
                    }
                }
            }
        }
    }

}


@ExperimentalFoundationApi
@Composable
@Preview
fun PreviewLobbyScreen() {
    LobbyScreen(
        LoginViewModel(mockDbClient),
        NavigationActions()
    )
}