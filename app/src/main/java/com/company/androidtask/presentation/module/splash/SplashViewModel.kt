package com.company.androidtask.presentation.module.splash

import com.company.androidtask.data.remote.model.LoginRequestModel
import com.company.androidtask.domain.LoginRepository
import com.company.androidtask.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : BaseViewModel() {

    private val _loginState = MutableStateFlow<Unit?>(null)
    val loginState: StateFlow<Unit?> get() = _loginState

    init {
        login()
    }

    private fun login() {
        execute({ loginRepository.login(LoginRequestModel.CREDENTIALS) }) {
            _loginState.emit(it.body())
        }
    }
}