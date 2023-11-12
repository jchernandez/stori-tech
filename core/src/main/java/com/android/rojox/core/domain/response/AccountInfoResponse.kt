package com.android.rojox.core.domain.response

import com.google.common.math.LongMath

data class AccountInfoResponse(
    val id: String,
    val name: String,
    val balance: Long,
    val number: String
) {
    companion object {
        fun fromMap(map: Map<String, Any>) = AccountInfoResponse(
            map["id"] as String,
            map["name"] as String,
            map["balance"] as Long,
            map["number"] as String
        )
    }
}
