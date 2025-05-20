package com.company.androidtask.data.remote.model

data class AuthInfoModel(
    val access_token: String,
    val expiresIn: Int,
    val tokenType: String,
    val scope: String?,
    val refreshToken: String
)