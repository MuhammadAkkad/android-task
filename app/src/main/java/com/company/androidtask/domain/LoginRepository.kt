package com.company.androidtask.domain

import com.company.androidtask.data.remote.model.LoginRequestModel

interface LoginRepository {

    suspend fun login(credentials: LoginRequestModel): Boolean

}