package com.android.rojox.core.domain.response

data class AuthenticationResponse(
    val token: String,
    val userName: String,
    val email: String
)
