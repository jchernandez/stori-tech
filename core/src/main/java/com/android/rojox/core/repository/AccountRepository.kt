package com.android.rojox.core.repository

import com.android.rojox.core.model.Account
import com.android.rojox.core.model.AccountMovement
import com.android.rojox.core.model.DataState

interface AccountRepository {

    suspend fun getAccountInfo(userId: String): DataState<Account>
    suspend fun getAccountMovements(accountId: String):  DataState<List<AccountMovement>>

}