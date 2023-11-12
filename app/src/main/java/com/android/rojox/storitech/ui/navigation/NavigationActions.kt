package com.android.rojox.storitech.ui.navigation

class NavigationActions (
    val onAction: (type: ActionType) -> Unit = {}
)

enum class ActionType {
    CLICK_LOGIN,
    CLICK_SIGN_IN,
    SIGN_IN_SUCCESS,
    VALIDATE_ACCOUNT,
    CLICK_BACK,
    CANCEL_ONBOARDING
}