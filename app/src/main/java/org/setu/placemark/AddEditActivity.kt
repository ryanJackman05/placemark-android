package org.setu.placemark

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts


class AddEditActivity : AppCompatActivity() {

    // 1 to 1 input fields with the add_edit view
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText
    private lateinit var imagePreview: ImageView
    private var selectedImageUri: Uri? = null;

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        titleInput =
            findViewById(R.id.titleInput)

        descriptionInput =
            findViewById(R.id.descriptionInput)

        xInput =
            findViewById(R.id.xInput)

        yInput =
            findViewById(R.id.yInput)

        val saveButton =
            findViewById<Button>(R.id.saveButton)

        val cancelButton =
            findViewById<Button>(R.id.cancelButton)

        editingId =
            intent.getLongExtra("id", -1L) // fetch extra value named id, will be present if this activity was called from editMark
                .takeIf { it != -1L } // takeif is a handy inline function for conditional yesOrNull returning

        if (editingId != null) {
            // Set to Edit mode
            findViewById<TextView>(R.id.formTitle)
                .text = "Edit Mark"

            loadExistingMark(editingId!!)

            Timber.d("Editing mark id=${editingId}")
        }

        saveButton.setOnClickListener {
            saveMark()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        imagePreview = findViewById(R.id.imageView)
        val imagePicker =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
                if (uri != null) {
                    // Keep access to the document URI for later use, when supported.
                    try {
                        contentResolver.takePersistableUriPermission(
                            uri,
                            android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (_: SecurityException) {
                        // Some providers may not grant persistable access.
                    }

                    selectedImageUri = uri
                    imagePreview.setImageURI(uri)
                }
            }

        findViewById<Button>(R.id.imageButton).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }
    }

    private fun loadExistingMark(id: Long) {

        val mark =
            AppData.placedMarks.findOne(id)

        if (mark == null) {

            Toast.makeText(
                this,
                "Mark not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        titleInput.setText(mark.title)
        descriptionInput.setText(mark.description)
        xInput.setText(mark.x.toString())
        yInput.setText(mark.y.toString())
        selectedImageUri = mark.imageUri?.let { Uri.parse(it) }

        if (selectedImageUri != null) {
            imagePreview.setImageURI(selectedImageUri)
        } else {
            imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
        }

    }

    private fun saveMark() {

        val title =
            titleInput.text.toString().trim()

        val description =
            descriptionInput.text.toString().trim()

        if (title.isEmpty()) {
            titleInput.error = "Title is required"
            return
        }

        val x =
            xInput.text.toString().toDoubleOrNull()

        if (x == null) {
            xInput.error = "Enter a valid number"
            return
        }

        val y =
            yInput.text.toString().toDoubleOrNull()

        if (y == null) {
            yInput.error = "Enter a valid number"
            return
        }

        if (editingId == null) {

            val mark = PlacemarkModel(
                title = title,
                description = description,
                x = x,
                y = y,
                imageUri = selectedImageUri?.toString()
            )

            AppData.placedMarks.create(mark)
            Timber.d("Created mark id=${mark.id}, title=${mark.title}")

            Toast.makeText(
                this,
                "Mark created",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val mark = PlacemarkModel(
                id = editingId!!,
                title = title,
                description = description,
                x = x,
                y = y,
                imageUri = selectedImageUri?.toString()
            )

            AppData.placedMarks.update(mark)
            Timber.d("Edited mark id=${mark.id}, title=${mark.title}")

            Toast.makeText(
                this,
                "Mark updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}
