package com.company.androidtask.data.workers

import android.content.Context
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
            val tasksResponse = tasksRepository.getTasks()
            if (tasksResponse?.isSuccessful == true) {
                Result.success()
            } else {
                loginRepository.login(LoginRequestModel.CREDENTIALS)
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "UpdateTasksWorker"
    }
}

