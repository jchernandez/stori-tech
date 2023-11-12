package com.android.rojox.storitech.ui.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.android.rojox.core.model.UserSession
import com.android.rojox.storitech.activities.HomeActivity
import com.android.rojox.storitech.activities.LobbyActivity
import com.android.rojox.storitech.ui.screens.*
import com.android.rojox.storitech.utils.composable


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun LobbyNavigation(activity: LobbyActivity) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavItem.Main.baseRoute
    ) {
        composable(NavItem.Main) {
            LobbyScreen(activity.loginViewModel, NavigationActions { action ->
                if(action == ActionType.CLICK_LOGIN) {
                    startHomeActivity(
                        activity,
                        activity.loginViewModel.userSession.value!!.data!!)
                } else  {
                    navController.navigate(NavItem.Onboarding.baseRoute)
                }
            })
        }

        composable(NavItem.Onboarding) {
            OnboardingScreen(activity.onboardingViewModel, NavigationActions { action ->
                if (action == ActionType.SIGN_IN_SUCCESS) {
                    navController.popBackStack()
                    navController.navigate(NavItem.ImageValidation.baseRoute)
                } else {
                    navController.popBackStack()
                }
            })
        }

        composable(NavItem.ImageValidation) {
            ValidateUserScreen(activity.onboardingViewModel, NavigationActions { action ->
                if (action == ActionType.VALIDATE_ACCOUNT) {
                    startHomeActivity(
                        activity,
                        activity.onboardingViewModel.userSession.value!!.data!!)
                } else {
                    activity.onboardingViewModel.clearData()
                    navController.popBackStack()
                }
            })
        }
    }
}

fun startHomeActivity(activity: LobbyActivity, userSession: UserSession) {
    val intent = Intent(activity, HomeActivity::class.java)
    intent.putExtra(HomeActivity.SESSION, userSession)
    activity.startActivity(intent)
    activity.finish()
}
