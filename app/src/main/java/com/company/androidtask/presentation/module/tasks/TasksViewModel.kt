package com.company.androidtask.presentation.module.tasks

import com.company.androidtask.data.remote.model.TasksModel
import com.company.androidtask.domain.TasksRepository
import com.company.androidtask.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : BaseViewModel() {

    private val _tasks = MutableStateFlow<List<TasksModel>?>(null)
    val tasks: StateFlow<List<TasksModel>?> get() = _tasks

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        execute({ tasksRepository.getTasks() }) {
            _tasks.emit(it?.body())
        }
    }
}