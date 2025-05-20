package com.company.androidtask.data.repository

import com.company.androidtask.data.db.TasksDao
import com.company.androidtask.data.remote.ApiService
import com.company.androidtask.data.remote.model.TasksModel
import com.company.androidtask.domain.TasksRepository
import retrofit2.Response
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: TasksDao
) : TasksRepository {

    override suspend fun getTasks(): Response<List<TasksModel>> {
        val response = apiService.getTasks()
        response.body()?.let { tasks ->
            dao.insertTasks(tasks)
        }
        return response
    }

}