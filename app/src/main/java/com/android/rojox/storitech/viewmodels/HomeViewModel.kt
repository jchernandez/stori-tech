package com.android.rojox.storitech.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.rojox.core.model.Account
import com.android.rojox.core.model.AccountMovement
import com.android.rojox.core.model.DataState
import com.android.rojox.core.model.DataStatus
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.repository.impl.AccountRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    dbClient: DbClient
): ViewModel() {

    private val accountRepository = AccountRepositoryImpl(dbClient)
    var accountData = MutableStateFlow(null as DataState<Account>?)
    var accountMovements = MutableStateFlow(null as DataState<List<AccountMovement>>?)

    fun getAccountInfo(userId: String) {
        viewModelScope.launch {
           accountData.value = accountRepository.getAccountInfo(userId)
            if (accountData.value?.status == DataStatus.SUCCESS) {
                getAccountMovements()
            }
        }
    }

    fun getAccountMovements() {
        viewModelScope.launch {
            val data = accountRepository.getAccountMovements(accountData.value!!.data!!.id)
            accountMovements.value = data
        }
    }

}