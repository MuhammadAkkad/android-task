package com.company.androidtask.data.remote.model

import kotlinx.serialization.SerialName

data class AuthInfoModel(
    val access_token: String,
    val expiresIn: Int,
    val tokenType: String,
    val scope: String?,
    val refreshToken: String
)