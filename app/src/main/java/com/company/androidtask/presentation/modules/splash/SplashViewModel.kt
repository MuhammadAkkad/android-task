package com.company.androidtask.presentation.modules.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.androidtask.data.remote.HeaderKey
import com.company.androidtask.data.manager.CacheManager
import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.domain.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: LoginRepository,
    cacheManager: CacheManager
) : ViewModel() {

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> get() = _isAuthorized

    init {
        cacheManager.set(HeaderKey.BASIC_AUTHENTICATION, "QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz")

        viewModelScope.launch {
            _isAuthorized.emit(repository.login(LoginRequestModel("365", "1")))
        }
    }
}