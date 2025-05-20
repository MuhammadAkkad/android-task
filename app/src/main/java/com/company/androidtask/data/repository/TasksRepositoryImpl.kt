package com.company.androidtask.data.repository

import com.company.androidtask.data.db.TasksDao
import com.company.androidtask.data.manager.CacheManager
import com.company.androidtask.data.remote.ApiService
import com.company.androidtask.data.remote.HeaderKey
import com.company.androidtask.data.remote.model.TasksModel
import com.company.androidtask.domain.TasksRepository
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val cacheManager: CacheManager,
    private val dao: TasksDao
) : TasksRepository {

    override suspend fun getTasks(): Response<List<TasksModel>> {
        val header = cacheManager.get<String>(HeaderKey.BEARER_AUTHENTICATION)
            ?: return Response.error(401, "Unauthorized".toResponseBody(null))

        val response = apiService.getTasks("Bearer $header")
        response.body()?.let { tasks ->
            dao.insertTasks(tasks)
        }
        return response
    }
}