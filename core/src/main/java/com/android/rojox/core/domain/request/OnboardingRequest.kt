package com.android.rojox.core.domain.request

data class OnboardingRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val validated: Boolean = false
)
