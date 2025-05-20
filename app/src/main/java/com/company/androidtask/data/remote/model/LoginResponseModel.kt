package com.company.androidtask.data.remote.model

import androidx.room.Entity

data class LoginResponseModel(
    val oauth: AuthInfoModel,
    val userInfo: UserInfoModel,
    val permissions: List<String>,
    val apiVersion: String,
    val showPasswordPrompt: Boolean
)
