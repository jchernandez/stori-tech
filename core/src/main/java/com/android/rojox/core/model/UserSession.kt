package com.android.rojox.core.model

import com.android.rojox.core.domain.response.AuthenticationResponse

data class UserSession(
    val token: String,
    val userName: String
): java.io.Serializable

fun AuthenticationResponse.toSession() = UserSession(
    token,
    userName
)