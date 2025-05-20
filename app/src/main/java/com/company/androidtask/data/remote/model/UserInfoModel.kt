package com.company.androidtask.data.remote.model

data class UserInfoModel(
    val personalNo: Int,
    val firstName: String,
    val lastName: String,
    val displayName: String,
    val active: Boolean,
    val businessUnit: String
)