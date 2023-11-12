package com.android.rojox.storitech.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem (val baseRoute: String) {
    object  Main: NavItem("main")
    object  Onboarding: NavItem("onboarding")
    object  ImageValidation: NavItem("imageValidation")

}