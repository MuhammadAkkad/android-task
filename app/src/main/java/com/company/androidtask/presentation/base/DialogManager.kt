package com.company.androidtask.presentation.base

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import com.company.androidtask.R
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class DialogManager @Inject constructor(
    @ActivityContext private val context: Context
) {

    fun showErrorDialog(
        @StringRes titleResId: Int,
        @StringRes descriptionResId: Int? = null,
        description: String? = null,
        @StringRes buttonTextResId: Int
    ) {
        val builder = AlertDialog.Builder(context, R.style.TransparentDialog)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_error, null)
        builder.setView(dialogView)

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val tvDescription = dialogView.findViewById<TextView>(R.id.tvDialogDescription)
        val btnAction = dialogView.findViewById<Button>(R.id.btnDialogAction)

        tvTitle.text = context.getString(titleResId)
        tvDescription.text = description ?: descriptionResId?.let { context.getString(it) }
        btnAction.text = context.getString(buttonTextResId)

        val dialog = builder.create()

        btnAction.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }
}