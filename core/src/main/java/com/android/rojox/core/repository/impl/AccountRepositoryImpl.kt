package com.android.rojox.core.repository.impl

import com.android.rojox.core.domain.response.AccountInfoResponse
import com.android.rojox.core.domain.response.MovementResponse
import com.android.rojox.core.model.Account
import com.android.rojox.core.model.AccountMovement
import com.android.rojox.core.model.DataState
import com.android.rojox.core.model.toModel
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.repository.AccountRepository
import com.android.rojox.core.utils.CoreException

class AccountRepositoryImpl(private val dbClient: DbClient): AccountRepository {

    override suspend fun getAccountInfo(userId: String): DataState<Account> {
        return try {
            DataState.success(dbClient.getAccountInfo(userId).toModel())
        } catch (e: CoreException) {
            DataState.error(e)
        }
    }

    override suspend fun getAccountMovements(accountId: String): DataState<List<AccountMovement>> {
        return try {
            DataState.success(dbClient.getAccountMovements(accountId).map {
                it.toModel()
            })
        } catch (e: CoreException) {
            DataState.error(e)
        }
    }
}