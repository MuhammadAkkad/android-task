package com.company.androidtask.data.remote.model

data class LoginRequestModel(
    val username: String,
    val password: String
) {
    companion object {
        val CREDENTIALS = LoginRequestModel("365", "1")
    }
}
