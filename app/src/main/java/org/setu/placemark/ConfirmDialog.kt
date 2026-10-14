package org.setu.placemark

import android.content.Context
import android.app.Dialog
import android.widget.Button
import android.widget.TextView

class ConfirmDialog (private val context: Context) {
    fun show(
        headerText: String,
        dialogText: String,
        confirmText: String = "Confirm",
        cancelText: String = "Cancel",
        onConfirm: () -> Unit
    ) {
        val dialog = Dialog(context) //
        dialog.setContentView(R.layout.confirm_dialog)
    }
}