package com.android.rojox.core.domain.response

import com.google.firebase.Timestamp

data class MovementResponse(
    val type: String,
    val description: String,
    val amount: Long,
    val timestamp: Timestamp,
    val accountId: String
) {
    companion object {
        fun fromMap(map: Map<String, Any>): MovementResponse {
            return MovementResponse(
                map["type"] as String,
                map["description"] as String,
                map["amount"] as Long,
                map["timestamp"] as Timestamp,
                map["accountId"] as String
            )
        }
    }
}
