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
                return if (!accessToken.isNullOrEmpty()) {
                    cacheManager.set(CacheKey.BEARER_AUTHENTICATION, accessToken)
                    Response.success(accessToken)
                } else {
                    handleFailedLoginAttemptWithCache(
                        response.code(),
                        "no valid access token provided."
                    )
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: response.message()
                return handleFailedLoginAttemptWithCache(
                    response.code(),
                    errorMessage ?: "Unknown login error"
                )
            }
        } catch (e: IOException) {
            return handleFailedLoginAttemptWithCache(
                503,
                "No internet connection and no cached login."
            )
        }
    }

    private fun handleFailedLoginAttemptWithCache(
        errorCode: Int,
        errorMessage: String
    ): Response<String?> {
        val cachedAccessToken = cacheManager.get<String>(CacheKey.BEARER_AUTHENTICATION)
        return if (!cachedAccessToken.isNullOrEmpty()) {
            Response.success(cachedAccessToken)
        } else {
            Response.error(
                errorCode,
                errorMessage.toResponseBody(null)
            )
        }
    }
}