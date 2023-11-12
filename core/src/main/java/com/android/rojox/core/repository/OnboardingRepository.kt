package com.android.rojox.core.repository

import com.android.rojox.core.model.DataState
import com.android.rojox.core.model.UserSession

interface OnboardingRepository {

    suspend fun registerUser(name: String, surname: String,email: String,password: String): DataState<UserSession>

    suspend fun uploadImage(userId: String, byteArray: ByteArray): DataState<String>

    suspend fun validateUser(userId: String): DataState<Unit>
}