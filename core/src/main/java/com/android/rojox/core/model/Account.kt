package com.android.rojox.core.model

import com.android.rojox.core.domain.response.AccountInfoResponse

data class Account(
    val id: String,
    val name: String,
    val number: String,
    val balance: Double
)

fun AccountInfoResponse.toModel() = Account(
    id,
    name,
    number,
    balance.toDouble()/100,
)