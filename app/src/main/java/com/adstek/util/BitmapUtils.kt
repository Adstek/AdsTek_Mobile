package com.adstek.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


object BitmapUtils {

    fun encodeBitmapToBase64String(image: Bitmap, quality: Int = 100, compressFormat: CompressFormat? = CompressFormat.JPEG): String {
        val byteArrayOS = ByteArrayOutputStream()
        if (compressFormat != null) {
            image.compress(compressFormat, quality, byteArrayOS)
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
    }

    fun decodeBase64StringToBitmap(input: String?): Bitmap {
        val decodedBytes = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun getBitmapFromFile(path: String?): Bitmap? {
        return try {
            BitmapFactory.decodeStream(FileInputStream(path?.let { File(it) }))
        } catch (e: Exception) {
            return null
        }
    }

}