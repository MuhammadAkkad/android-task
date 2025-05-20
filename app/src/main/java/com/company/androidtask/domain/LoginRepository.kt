package com.company.androidtask.domain

import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.data.remote.model.LoginResponseModel
import retrofit2.Response

interface LoginRepository {

    suspend fun login(credentials: LoginRequestModel): Response<LoginResponseModel>?

}