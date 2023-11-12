package com.android.rojox.core.model

import com.android.rojox.core.domain.response.MovementResponse


data class AccountMovement(
    val type: MovementType,
    val amount: Double,
    val timeStamp: Long,
    val description: String
)

enum class MovementType {
    INCOME, OUTCOME
}

fun MovementResponse.toModel() = AccountMovement(
    MovementType.valueOf(type),
    amount.toDouble()/100,
    timestamp.toDate().time,
    description
)