package com.company.androidtask.presentation.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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