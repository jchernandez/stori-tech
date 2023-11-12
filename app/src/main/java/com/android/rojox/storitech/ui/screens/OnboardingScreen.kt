package com.android.rojox.storitech.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.android.rojox.core.model.DataStatus
import com.android.rojox.storitech.BuildConfig
import com.android.rojox.storitech.R
import com.android.rojox.storitech.ui.UiApp
import com.android.rojox.storitech.ui.components.ProgressDialog
import com.android.rojox.storitech.ui.components.SecureTextField
import com.android.rojox.storitech.ui.components.TextField
import com.android.rojox.storitech.ui.navigation.ActionType
import com.android.rojox.storitech.ui.navigation.NavigationActions
import com.android.rojox.storitech.utils.ImageHelper
import com.android.rojox.storitech.utils.mockDbClient
import com.android.rojox.storitech.utils.mockStorageClient
import com.android.rojox.storitech.utils.observeAsActions
import com.android.rojox.storitech.viewmodels.OnboardingViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    actions: NavigationActions = NavigationActions()
) {

    val context = LocalContext.current
    val name by viewModel.name.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isRegisterBtnEnabled by viewModel.isRegisterBtnEnabled.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    viewModel.userSession.observeAsActions {
        if (it?.status == DataStatus.SUCCESS) {
            actions.onAction(ActionType.SIGN_IN_SUCCESS)
        } else if (it?.status == DataStatus.ERROR) {
            Toast.makeText(context, it.error!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    UiApp {

        if (isLoading) {
            ProgressDialog()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            IconButton(onClick = {
                actions.onAction(ActionType.CLICK_BACK)
            }) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "")
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                    ) {

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = name,
                        onValueChange = { viewModel.name.value = it },
                        placeholder = stringResource(id = R.string.name)
                    )

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = lastName,
                        onValueChange = { viewModel.lastName.value = it },
                        placeholder = stringResource(id = R.string.lastName)
                    )

                    TextField(
                        value = email,
                        onValueChange = { viewModel.email.value = it },
                        placeholder = stringResource(id = R.string.email),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
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


                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = { viewModel.registerUser() },
                    enabled = isRegisterBtnEnabled
                ) {
                    Text(stringResource(id = R.string.create_account))
                }
            }
        }
    }
}



@ExperimentalFoundationApi
@Composable
@Preview
fun PreviewOnboardingScreen() {
    OnboardingScreen(OnboardingViewModel(mockDbClient, mockStorageClient))
}