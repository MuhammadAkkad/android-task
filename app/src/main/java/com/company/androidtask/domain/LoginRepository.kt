package com.company.androidtask.domain

import com.company.androidtask.data.remote.model.LoginRequestModel
import retrofit2.Response

interface LoginRepository {

    suspend fun login(credentials: LoginRequestModel): Response<Unit>

}