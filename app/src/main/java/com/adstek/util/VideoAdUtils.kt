package com.adstek.util

import android.content.Context
import android.os.Environment
import android.util.Log
import com.adstek.data.local.model.VideoAd
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException

fun VideoAd.downloadVideo(context: Context, callback: (Boolean) -> Unit) {
    val TAG = "DownloadVideo"

    val request = Request.Builder().url(this.video).build()
    val client = OkHttpClient()

    Log.d(TAG, "Video download started: " + video)
    client.newCall(request).enqueue(object : okhttp3.Callback {

        override fun onFailure(call: okhttp3.Call, e: IOException) {
            Log.d(TAG, "Video downloading failed: " + video)
            callback(false)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (!response.isSuccessful) {
                Log.d(TAG, "Video downloading failed: " + video + " res code: " + response.code)
                callback(false)
                return
            }
            response.body?.let { body ->
                try {
                    val videoAdFile = createVideoAdFile(context, getFileName())
                    videoAdFile.outputStream().use { fileOut ->
                        body.byteStream().copyTo(fileOut)
                    }
                    localVideo = videoAdFile.absolutePath
                    Log.d(TAG, "Video downloading successful: " + videoAdFile.absolutePath)
                    callback(true)
                } catch (e: Exception) {
                    Log.d(TAG, "Error writing video file: ${e.message}")
                    callback(false)
                }
            } ?: run {
                Log.d(TAG, "Video downloading failed: $video, response body is null")
                callback(false)
            }
        }
    })
}


fun downloadVideo(context: Context, videoUrl: String, callback: (Boolean) -> Unit) {
    val TAG = "DownloadVideo"

    val request = Request.Builder().url(videoUrl).build()
    val client = OkHttpClient()

    Log.d(TAG, "Video download started: " + videoUrl)
    client.newCall(request).enqueue(object : okhttp3.Callback {

        override fun onFailure(call: okhttp3.Call, e: IOException) {
            Log.d(TAG, "Video downloading failed: " + videoUrl)
            callback(false)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (!response.isSuccessful) {
                Log.d(TAG, "Video downloading failed: " + videoUrl + " res code: " + response.code)
                callback(false)
                return
            }
            response.body?.let { body ->
                try {
                    val videoAdFile = createVideoAdFile(context, getFileName(videoUrl))
                    videoAdFile.outputStream().use { fileOut ->
                        body.byteStream().copyTo(fileOut)
                    }
                    Log.d(TAG, "Video downloading successful: " + videoAdFile.absolutePath)
                    callback(true)
                } catch (e: Exception) {
                    Log.d(TAG, "Error writing video file: ${e.message}")
                    callback(false)
                }
            } ?: run {
                Log.d(TAG, "Video downloading failed: $videoUrl, response body is null")
                callback(false)
            }
        }
    })
}


fun createVideoAdFile(context: Context, fileName: String): File {
    val adsTekSysDir = context.filesDir

    val customDir = File(adsTekSysDir, "videoAds")
    if (!customDir.exists()) {
        customDir.mkdirs()
    }
    val videoFile = File(customDir, fileName)
    return videoFile
}

fun getVideoAdFile(context: Context, fileName: String): File {
    val adsTekSysDir = context.filesDir
    val customDir = File(adsTekSysDir, "videoAds")
    return File(customDir, fileName)
}

fun VideoAd.getFileName(): String {
    return video.substringAfterLast('/').substringBefore('?')
}

fun getFileName(link: String): String {
    return link.substringAfterLast('/').substringBefore('?')
}