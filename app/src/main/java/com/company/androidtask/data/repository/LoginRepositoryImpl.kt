package com.company.androidtask.data.repository

import com.company.androidtask.data.manager.CacheKey
import com.company.androidtask.data.manager.CacheManager
import com.company.androidtask.data.remote.ApiService
import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.data.remote.model.LoginResponseModel
import com.company.androidtask.domain.LoginRepository
import retrofit2.Response
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val cacheManager: CacheManager
) : LoginRepository {

    override suspend fun login(credentials: LoginRequestModel): Response<LoginResponseModel> {
        val response = apiService.login(credentials)
        response.body()?.oauth?.let {
            cacheManager.set(CacheKey.ACCESS_TOKEN, it.accessToken)
        }
        return response
    }
}