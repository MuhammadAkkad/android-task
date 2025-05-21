package com.company.androidtask.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState: StateFlow<UIState> get() = _uiState.asStateFlow()

    protected fun <T> execute(
        request: suspend () -> T,
        updateState: suspend (T) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.emit(UIState.Loading)

            try {
                val result = withContext(Dispatchers.IO) { request() }

                if (result is Response<*>) {
                    if (result.isSuccessful) {
                        updateState.invoke(result)
                    } else {
                        val errorBodyString = try {
                            result.errorBody()?.string()
                        } catch (e: IOException) {
                            null
                        }

                        val errorMessage = errorBodyString ?: result.message() ?: "Unknown error"
                        _uiState.emit(UIState.Error("${result.code()} - $errorMessage"))
                    }
                } else {
                    updateState.invoke(result)
                }

            } catch (e: Exception) {
                _uiState.emit(UIState.Error(e.localizedMessage ?: "An unexpected error occurred"))

            } finally {
                _uiState.emit(UIState.Idle)
            }
        }
    }
}