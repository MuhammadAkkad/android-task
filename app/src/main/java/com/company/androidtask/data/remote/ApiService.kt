package com.company.androidtask.data.remote

import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.data.remote.model.LoginResponseModel
import com.company.androidtask.data.remote.model.TasksModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("index.php/login")
    suspend fun login(
        @Header("Authorization") token: String,
        @Body request: LoginRequestModel
    ): Response<LoginResponseModel>

    @GET("dev/index.php/v1/tasks/select")
    suspend fun getTasks(
        @Header("Authorization") token: String
    ): Response<List<TasksModel>>

}