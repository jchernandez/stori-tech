package com.android.rojox.core.repository

import com.android.rojox.core.model.DataState
import com.android.rojox.core.model.UserSession

interface LoginRepository {
    suspend fun login(email: String, password: String): DataState<UserSession>
}