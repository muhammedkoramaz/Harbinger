package com.sn.harbinger.util

import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import java.io.ByteArrayOutputStream

object IOUtil {

    fun bitmapConvertToBase64(bitmap: Bitmap): String {
        val maxSize = 300
        var width = bitmap.width
        var height = bitmap.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }

        val resizedBitmap: Bitmap?
        var byteArray: ByteArray? = null

        try {
            resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArray = byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.util.Base64.getEncoder().encodeToString(byteArray)
        } else {
            Base64.encodeToString(byteArray, Base64.NO_WRAP)
        }
    }

}