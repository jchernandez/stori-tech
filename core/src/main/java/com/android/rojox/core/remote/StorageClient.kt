package com.android.rojox.core.remote

interface StorageClient {
    suspend fun uploadFile(userId: String, byteArray: ByteArray): String
}