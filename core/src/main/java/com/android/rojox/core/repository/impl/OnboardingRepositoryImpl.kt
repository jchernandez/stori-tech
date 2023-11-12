package com.android.rojox.core.repository.impl


import com.android.rojox.core.domain.request.OnboardingRequest
import com.android.rojox.core.model.DataState
import com.android.rojox.core.model.UserSession
import com.android.rojox.core.model.toSession
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.remote.StorageClient
import com.android.rojox.core.repository.OnboardingRepository
import com.android.rojox.core.utils.CoreException

class OnboardingRepositoryImpl(
    private val dbClient: DbClient,
    private val storageClient: StorageClient
) : OnboardingRepository {

    override suspend fun registerUser(
        name: String,
        surname: String,
        email: String,
        password: String
    ): DataState<UserSession> {
        return try {
            DataState.success(
                dbClient.registerUser(OnboardingRequest(
                    name, surname, email, password
                )).toSession()
            )
        } catch (e: CoreException) {
            DataState.error(e)
        }
    }

    override suspend fun uploadImage(userId: String, byteArray: ByteArray): DataState<String> {
        return try {
            DataState.success(storageClient.uploadFile(userId, byteArray))
        } catch (e: CoreException) {
            DataState.error(e)
        }
    }

    override suspend fun validateUser(userId: String): DataState<Unit> {
        return try {
            DataState.success(dbClient.validateAccount(userId))
        } catch (e: CoreException) {
            DataState.error(e)
        }
    }


}