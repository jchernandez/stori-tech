package com.android.rojox.storitech.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileDescriptor
import java.io.IOException

object ImageHelper {

    @Throws(IOException::class)
    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        if (uri.scheme == "content") {
            return getBitmapFromContentUri(context.contentResolver, uri)
        }
        return BitmapFactory.decodeFile(uri.path)
    }

    @Throws(IOException::class)
    private fun getBitmapFromContentUri(contentResolver: ContentResolver, contentUri: Uri): Bitmap? {
        contentResolver.openFileDescriptor(contentUri, "r")?.use { parcelFileDescriptor ->
            val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
            return BitmapFactory.decodeFileDescriptor(fileDescriptor)
        }
        return null
    }
}
