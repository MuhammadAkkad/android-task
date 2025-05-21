package com.company.androidtask.presentation.common.extensions

fun String?.orHyphen(): String {
    return if (this.isNullOrEmpty()) {
        "-"
    } else {
        this
    }
}