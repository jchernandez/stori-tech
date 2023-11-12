package com.android.rojox.core.repository.impl

import com.android.rojox.core.domain.request.LoginRequest
import com.android.rojox.core.domain.response.AuthenticationResponse
import com.android.rojox.core.model.DataState
import com.android.rojox.core.model.UserSession
import com.android.rojox.core.model.toSession
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.repository.LoginRepository
import com.android.rojox.core.utils.CoreException

class LoginRepositoryImpl(
    private val dbClient: DbClient
): LoginRepository {
    override suspend fun login(email: String, password: String): DataState<UserSession> {
        return try {
            DataState.success(dbClient.login(LoginRequest(email, password)).toSession())
        } catch (ex: CoreException) {
            DataState.error(ex)
        }
    }
}