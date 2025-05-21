package com.company.androidtask.presentation.common.helper

import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class DialogHelper @Inject constructor(
    private val fragment: Fragment
) {
    fun showDialog(
        title: String,
        message: String,
        positiveButtonText: String? = null,
        negativeButtonText: String? = null,
        onPositiveButtonClick: (() -> Unit)? = null,
        onNegativeButtonClick: (() -> Unit)? = null,
    ) {
        fragment.context?.let { context ->
            MaterialAlertDialogBuilder(context).apply {
                setTitle(title)
                setMessage(message)

                positiveButtonText?.let {
                    setPositiveButton(it) { _, _ -> onPositiveButtonClick?.invoke() }
                }

                negativeButtonText?.let {
                    setNegativeButton(it) { dialog, _ ->
                        onNegativeButtonClick?.invoke()
                        dialog.dismiss()
                    }
                }
            }.show()
        }
    }
}
