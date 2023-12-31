package com.android.rojox.storitech.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.android.rojox.storitech.ui.navigation.ActionType
import com.android.rojox.storitech.ui.navigation.NavigationActions
import com.android.rojox.storitech.utils.ImageHelper
import com.android.rojox.storitech.utils.mockDbClient
import com.android.rojox.storitech.utils.mockStorageClient
import com.android.rojox.storitech.utils.observeAsActions
import com.android.rojox.storitech.viewmodels.OnboardingViewModel
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ValidateUserScreen(viewModel: OnboardingViewModel, actions: NavigationActions =  NavigationActions()) {

    val isLoading by viewModel.isLoading.collectAsState()

    var isValidated by remember {
        mutableStateOf(false)
    }

    viewModel.validateUser.observeAsActions {
        if (it?.status == DataStatus.SUCCESS) {
            isValidated = true
        }
    }

    BackHandler {
    }

    UiApp {

        if (isLoading) {
            ProgressDialog()
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {

            if (! isValidated) {
                ImageUploadScreen(viewModel = viewModel) {
                    actions.onAction(ActionType.CANCEL_ONBOARDING)
                }
            } else {
               DoneScreen(
                   modifier = Modifier.align(Center)
               ) {
                   actions.onAction(ActionType.VALIDATE_ACCOUNT)
               }
            }


        }


    }
}


@Composable
private fun DoneScreen(modifier: Modifier = Modifier, onFinish:() -> Unit) {
    LaunchedEffect(true) {
        for (i in 0 until 3) {
            delay(1000)
        }
        onFinish()
    }
    Column(
        modifier
    ) {
        Text(
            text = stringResource(id = R.string.user_registred),
            color = Color.Green,
            style = MaterialTheme.typography.h5
        )
        Image(
            modifier = Modifier.size(100.dp),
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.Green)
        )
    }
}


@Composable
fun ImageUploadScreen(viewModel: OnboardingViewModel, onCanelOnboarding:() -> Unit) {
    val context = LocalContext.current

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val file = createImageFile(context)
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
            try {
                val bitmap = ImageHelper.getBitmapFromUri(context, uri)
                val baos = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                viewModel.imageByteArray.value = baos.toByteArray()
            } catch (e: Exception) {
                Log.e("Onboarding", e.message?:"Error")
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val onClickImageRequest =  {
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val buttonStringId = if (capturedImageUri.path?.isNotEmpty() == true) {
        R.string.retake_image
    } else R.string.take_id_photo


    Box {
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onCanelOnboarding
        ) {
            Icon(imageVector = Icons.Outlined.Close, contentDescription = "")
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .align(Alignment.Center)
        ) {
            if (capturedImageUri.path?.isNotEmpty() == true) {
                Image(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(16.dp, 8.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = rememberAsyncImagePainter(capturedImageUri),
                    contentDescription = null
                )
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onClickImageRequest) {
                Text(text = stringResource(id = buttonStringId))
            }


            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = capturedImageUri.path?.isNotEmpty() == true,
                onClick = {
                    viewModel.uploadImage()
                }) {
                Text(text = stringResource(id = R.string.validate_image))
            }
        }
    }
}

private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat(
        "yyyyMMdd_HHmmss",
        Locale.getDefault()
    ).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        context.externalCacheDir
    )
}


@ExperimentalFoundationApi
@Composable
@Preview
fun PreviewImageUploadScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        DoneScreen(
            modifier = Modifier.align(Center)
        ) {
        }
    }
    /*
    ValidateUserScreen(
        OnboardingViewModel(
            mockDbClient, mockStorageClient))*/
}