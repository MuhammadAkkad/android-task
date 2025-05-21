package com.company.androidtask.presentation.module.splash

import com.company.androidtask.data.common.Constants.API_KEY
import com.company.androidtask.data.manager.CacheKey
import com.company.androidtask.data.manager.CacheManager
import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.domain.LoginRepository
import com.company.androidtask.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    cacheManager: CacheManager
) : BaseViewModel() {

    private val _loginState = MutableStateFlow<String?>(null)
    val loginState: StateFlow<String?> get() = _loginState

    init {
        cacheManager.set(CacheKey.BASIC_AUTHENTICATION, API_KEY)
        login()
    }

    private fun login() {
        execute({ loginRepository.login(LoginRequestModel.CREDENTIALS) }) {
            _loginState.emit(it.body())
        }
    }
}