package com.company.androidtask.data.repository

import com.company.androidtask.data.manager.CacheKey
import com.company.androidtask.data.manager.CacheManager
import com.company.androidtask.data.remote.ApiService
import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.domain.LoginRepository
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val cacheManager: CacheManager
) : LoginRepository {

    override suspend fun login(credentials: LoginRequestModel): Response<String?> {
        val header = cacheManager.get<String>(CacheKey.BASIC_AUTHENTICATION)
            ?: return Response.error(
                401,
                "Basic authentication header missing".toResponseBody(null)
            )

        try {
            val response = apiService.login("Basic $header", credentials)

            if (response.isSuccessful) {
                val accessToken = response.body()?.oauth?.access_token
                if (!accessToken.isNullOrEmpty()) {
                    cacheManager.set(CacheKey.BEARER_AUTHENTICATION, accessToken)
                    return Response.success(response.body()?.oauth?.access_token)
                } else {

                    val cachedAccessToken = cacheManager.get<String>(CacheKey.BEARER_AUTHENTICATION)
                    return if (!cachedAccessToken.isNullOrEmpty()) {
                        Response.success(cachedAccessToken)
                    } else {
                        Response.error(
                            response.code(),
                            "no valid access token provided.".toResponseBody(null)
                        )
                    }
                }
            } else {
                val cachedAccessToken = cacheManager.get<String>(CacheKey.BEARER_AUTHENTICATION)
                return if (!cachedAccessToken.isNullOrEmpty()) {
                    Response.success(cachedAccessToken)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: response.message()
                    Response.error(
                        response.code(),
                        errorMessage?.toResponseBody(null) ?: "Unknown login error".toResponseBody(
                            null
                        )
                    )
                }
            }
        } catch (e: IOException) {
            val cachedAccessToken = cacheManager.get<String>(CacheKey.BEARER_AUTHENTICATION)
            return if (!cachedAccessToken.isNullOrEmpty()) {
                Response.success(cachedAccessToken)
            } else {
                Response.error(
                    503,
                    "No internet connection and no cached login.".toResponseBody(null)
                )
            }
        }
    }
}