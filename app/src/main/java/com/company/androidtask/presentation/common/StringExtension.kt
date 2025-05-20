package com.company.androidtask.presentation.common

fun String?.orHyphen(): String {
    return if (this.isNullOrEmpty()) {
        "-"
    } else {
        this
    }
}