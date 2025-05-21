package com.company.androidtask.data.remote.model

data class AuthInfoModel(
    val access_token: String,
    val expiresIn: Int? = null,
    val tokenType: String? = null,
    val scope: String? = null,
    val refreshToken: String? = null,
)