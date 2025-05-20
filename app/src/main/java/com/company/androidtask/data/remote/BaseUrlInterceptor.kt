package com.company.androidtask.data.remote

import com.company.androidtask.data.manager.CacheKey
import com.company.androidtask.data.manager.CacheManager
import com.company.androidtask.data.manager.HeaderKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(
    private val cacheManager: CacheManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = cacheManager.get<String>(CacheKey.ACCESS_TOKEN)

        if (accessToken.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        val updatedRequest = originalRequest.newBuilder().apply {
            addHeader(HeaderKey.AUTHORIZATION, "Bearer $accessToken")
        }.build()

        val response = chain.proceed(updatedRequest)

        if (response.code == 401) {
            cacheManager.remove(CacheKey.ACCESS_TOKEN)
        }

        return response
    }
}