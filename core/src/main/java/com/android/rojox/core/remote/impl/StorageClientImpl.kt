package com.android.rojox.core.remote.impl

import com.android.rojox.core.remote.StorageClient
import com.google.firebase.storage.FirebaseStorage

class StorageClientImpl(private val storage: FirebaseStorage): StorageClient {

    override suspend fun uploadFile(userId: String, byteArray: ByteArray): String {

        val imagePath = "idImages/${userId}_id.jpg"
        val storageRef = storage.reference
        val userImageIdRef = storageRef.child(imagePath)

        userImageIdRef.putBytes(byteArray)

        return userImageIdRef.path
    }
}