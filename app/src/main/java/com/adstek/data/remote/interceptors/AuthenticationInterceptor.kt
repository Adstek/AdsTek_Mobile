package com.adstek.data.remote.interceptors

import com.adstek.util.Constants
import com.adstek.util.SharedPref
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class AuthenticationInterceptor @Inject constructor(
    private val userFunctions: SharedPref
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authKey = userFunctions.getPref(Constants.KEY_ACCESS_TOKEN, null)
        var request = chain.request()


        if (authKey.isNullOrEmpty()) {
            request = request.newBuilder()
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .addHeader("Accept", "*/*")
                .build()
        } else {
            Timber.tag("authKey").d("Bearer $authKey")
            request = request.newBuilder()
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .addHeader("Accept", "*/*")
                .build()

            if (request.headers["Authorization"].isNullOrEmpty()) {
                request = request.newBuilder()
                    .addHeader("Authorization", "JWT $authKey")
                    .build()
            }
        }
        return chain.proceed(request)
    }

    companion object {
        private const val CONTENT_TYPE_JSON = "application/json"
    }
}