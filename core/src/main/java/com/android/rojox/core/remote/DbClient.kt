package com.android.rojox.core.remote

import com.android.rojox.core.domain.request.LoginRequest
import com.android.rojox.core.domain.request.OnboardingRequest
import com.android.rojox.core.domain.response.AccountInfoResponse
import com.android.rojox.core.domain.response.AuthenticationResponse
import com.android.rojox.core.domain.response.MovementResponse

interface DbClient {

    suspend fun registerUser(onboardingRequest: OnboardingRequest): AuthenticationResponse

    suspend fun login(loginRequest: LoginRequest): AuthenticationResponse

    suspend fun getAccountInfo(userId: String): AccountInfoResponse

    suspend fun getAccountMovements(accountId: String): List<MovementResponse>

    suspend fun validateAccount(userId: String): Unit
}