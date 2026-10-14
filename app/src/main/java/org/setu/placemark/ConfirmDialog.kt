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
        onConfirm: () -> Unit // configurable lambda for the confirm action
    ) {
        val dialog = Dialog(context) //
        dialog.setContentView(R.layout.confirm_dialog)

        val header = dialog.findViewById<TextView>(R.id.headerText)
        val message = dialog.findViewById<TextView>(R.id.dialogText)

        val confirmButton = dialog.findViewById<Button>(R.id.dialogConfirmButton)
        val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelButton)

        confirmButton.setOnClickListener {
            dialog.dismiss()
            onConfirm() // the passed lambda
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}