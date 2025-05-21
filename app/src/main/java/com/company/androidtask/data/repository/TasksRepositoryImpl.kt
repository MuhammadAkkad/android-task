package com.company.androidtask.data.repository

import com.company.androidtask.data.db.TasksDao
import com.company.androidtask.data.manager.CacheKey
import com.company.androidtask.data.manager.CacheManager
import com.company.androidtask.data.remote.ApiService
import com.company.androidtask.data.remote.model.TasksModel
import com.company.androidtask.domain.TasksRepository
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val cacheManager: CacheManager,
    private val dao: TasksDao
) : TasksRepository {

    override suspend fun getTasks(): Response<List<TasksModel>> {
        val header = cacheManager.get<String>(CacheKey.BEARER_AUTHENTICATION)
            ?: return Response.error(401, "Unauthorized".toResponseBody(null))

        try {
            val response = apiService.getTasks("Bearer $header")

            if (response.isSuccessful) {
                response.body()?.let { tasks ->
                    dao.insertTasks(tasks)
                }
                return response
            } else {
                val localTasks = dao.getTasks()
                return if (localTasks.isNotEmpty()) {
                    Response.success(localTasks)
                } else {
                    Response.error(response.code(), response.message().toResponseBody(null))
                }
            }
        } catch (e: IOException) {
            val localTasks = dao.getTasks()
            return if (localTasks.isNotEmpty()) {
                Response.success(localTasks)
            } else {
                Response.error(503, "No internet connection and no cached data.".toResponseBody(null))
            }
        }
    }
}