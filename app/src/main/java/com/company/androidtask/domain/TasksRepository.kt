package com.company.androidtask.domain

import com.company.androidtask.data.remote.model.TasksModel
import retrofit2.Response

interface TasksRepository {

    suspend fun getTasks(): Response<List<TasksModel>>?

}