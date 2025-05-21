package com.company.androidtask.presentation.common.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

context(Fragment)
fun <T> Flow<T>.collectFlow(action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        collectLatest {
            action(it)
        }
    }
}

context(Fragment)
fun <T> Flow<T>.collectFlowOnStart(action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            collectLatest {
                action(it)
            }
        }
    }
}