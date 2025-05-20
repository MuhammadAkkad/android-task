package com.company.androidtask.data.repository

import com.company.androidtask.data.manager.CacheManager
import com.company.androidtask.data.remote.ApiService
import com.company.androidtask.data.remote.HeaderKey
import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.domain.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val cacheManager: CacheManager
) : LoginRepository {

    override suspend fun login(credentials: LoginRequestModel): Boolean {
        val header =
            cacheManager.get<String>(HeaderKey.BASIC_AUTHENTICATION) ?: return false
        val response = apiService.login("Basic $header", credentials)
        response.body()?.oauth?.let {
            if (it.access_token.isNotEmpty()) {
                cacheManager.set(HeaderKey.BEARER_AUTHENTICATION, it.access_token)
                return true
            }
        }
        return false
    }
}