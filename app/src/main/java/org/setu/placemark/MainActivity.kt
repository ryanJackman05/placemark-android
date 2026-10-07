package org.setu.placemark

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewMarksButton =
            findViewById<Button>(R.id.viewMarksButton)

        val addMarkButton =
            findViewById<Button>(R.id.addMarkButton)

        viewMarksButton.setOnClickListener {
            startActivity(
                Intent(this, MarkListActivity::class.java)
            )
        }

        addMarkButton.setOnClickListener {
            startActivity(
                Intent(this, AddEditActivity::class.java)
            )
        }

    }

    override fun onResume() {
        super.onResume()

        if (::listLayout.isInitialized) {
            displayMarks()
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val title = TextView(this).apply {
            text = "Placed Marks"
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val addButton = Button(this).apply {
            text = "Add Mark"
            setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                startActivity(intent)
            }
        }

        listLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            addButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            listLayout,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)

        displayMarks()
    }

    private fun displayMarks() {

        listLayout.removeAllViews()

        val marks = AppData.placedMarks.findAll()

        if (marks.isEmpty()) {

            val emptyText = TextView(this).apply {
                text = "No placed marks yet."
                textSize = 18f
                setPadding(0, 40, 0, 40)
            }

            listLayout.addView(emptyText)

            return
        }

        for (mark in marks) {

            val markLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 20, 0, 20)
            }

            val markTitle = TextView(this).apply {
                text = "${mark.id}: ${mark.title}"
                textSize = 20f
            }

            val markDescription = TextView(this).apply {
                text = mark.description
                textSize = 16f
            }

            val coordinates = TextView(this).apply {
                text = "X: ${mark.x}, Y: ${mark.y}"
                textSize = 14f
            }

            val editButton = Button(this).apply {
                text = "Edit"

                setOnClickListener {
                    val intent = Intent(
                        this@MainActivity,
                        AddEditActivity::class.java
                    )

                    intent.putExtra("id", mark.id)

                    startActivity(intent)
                }
            }

            val deleteButton = Button(this).apply {
                text = "Delete"

                setOnClickListener {
                    AppData.placedMarks.delete(mark.id)
                    displayMarks()
                }
            }

            markLayout.addView(markTitle)
            markLayout.addView(markDescription)
            markLayout.addView(coordinates)
            markLayout.addView(editButton)
            markLayout.addView(deleteButton)

            listLayout.addView(markLayout)
        }
    }
}
