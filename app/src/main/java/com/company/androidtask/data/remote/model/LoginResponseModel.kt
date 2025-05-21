package com.company.androidtask.data.remote.model

data class LoginResponseModel(
    val oauth: AuthInfoModel,
    val userInfo: UserInfoModel? = null,
    val permissions: List<String>? = null,
    val apiVersion: String? = null,
    val showPasswordPrompt: Boolean? = null
)
