package com.android.rojox.storitech.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import coil.annotation.ExperimentalCoilApi
import com.android.rojox.core.CoreModule
import com.android.rojox.storitech.ui.navigation.LobbyNavigation
import com.android.rojox.storitech.viewmodels.LoginViewModel
import com.android.rojox.storitech.viewmodels.OnboardingViewModel

@OptIn(ExperimentalFoundationApi::class)
class LobbyActivity : AppCompatActivity() {

    val loginViewModel = LoginViewModel(CoreModule.dbClient)
    val onboardingViewModel = OnboardingViewModel(CoreModule.dbClient, CoreModule.storageClient)

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LobbyNavigation(this)
        }
    }
}