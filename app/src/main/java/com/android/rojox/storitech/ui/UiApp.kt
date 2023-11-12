package com.android.rojox.storitech.ui


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.rojox.storitech.ui.theme.AppTheme

@Composable
fun UiApp(content: @Composable () -> Unit) {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

