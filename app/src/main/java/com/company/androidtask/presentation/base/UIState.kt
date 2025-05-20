package com.company.androidtask.presentation.base

sealed class UIState {
    data object Idle : UIState()
    data object Loading : UIState()
    data class Error(val message: String) : UIState()
}