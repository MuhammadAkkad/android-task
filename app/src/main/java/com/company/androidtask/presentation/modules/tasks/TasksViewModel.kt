package com.company.androidtask.presentation.modules.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.androidtask.data.remote.model.TasksModel
import com.company.androidtask.domain.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TasksRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TasksModel>?>(null)
    val tasks: StateFlow<List<TasksModel>?> get() = _tasks

    init {
        viewModelScope.launch {
            _tasks.emit(repository.getTasks()?.body())
        }
    }
}