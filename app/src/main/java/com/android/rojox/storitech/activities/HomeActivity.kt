package com.android.rojox.storitech.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import com.android.rojox.core.CoreModule
import com.android.rojox.core.model.UserSession
import com.android.rojox.storitech.ui.screens.HomeScreen
import com.android.rojox.storitech.viewmodels.HomeViewModel

class HomeActivity: AppCompatActivity() {

    companion object {
        val SESSION = "USER_SESSION"
    }

    private val viewModel = HomeViewModel(CoreModule.dbClient)

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val session = intent.extras!!.getSerializable(SESSION) as UserSession
        setContent {
            HomeScreen(session, viewModel)
        }
    }
}