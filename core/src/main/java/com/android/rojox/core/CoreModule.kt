package com.android.rojox.core

import android.content.Context
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.remote.StorageClient
import com.android.rojox.core.remote.impl.DbClientImpl
import com.android.rojox.core.remote.impl.StorageClientImpl
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


object CoreModule  {

    lateinit var dbClient: DbClient
    lateinit var storageClient: StorageClient

    fun init(context: Context) {
        FirebaseApp.initializeApp(context)
        dbClient = DbClientImpl(Firebase.firestore)
        storageClient = StorageClientImpl(Firebase.storage)
    }
}