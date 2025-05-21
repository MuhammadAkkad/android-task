package com.company.androidtask.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.domain.LoginRepository
import com.company.androidtask.domain.TasksRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateTasksWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val loginRepository: LoginRepository,
    private val tasksRepository: TasksRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val loginResponse = loginRepository.login(LoginRequestModel.CREDENTIALS)
            if (!loginResponse.isSuccessful) {
                Log.e("UpdateTasksWorker", "Login failed with code: ${loginResponse.code()}")
                return Result.retry()
            }

            val tasksResponse = tasksRepository.getTasks()
            if (tasksResponse?.isSuccessful == true) {
                Log.i("UpdateTasksWorker", "Tasks successfully updated")
                Result.success()
            } else {
                Log.e(
                    "UpdateTasksWorker",
                    "Failed to fetch tasks with code: ${tasksResponse?.code()}"
                )
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("UpdateTasksWorker", "Exception in worker: ${e.message}", e)
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "UpdateTasksWorker"
    }
}

