package com.example.androidcrossstitchcounter

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddProjActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_proj_activity)

        val presentRBtn = findViewById<RadioButton>(R.id.current)
        val futureRBtn = findViewById<RadioButton>(R.id.future)
        val finishRBtn = findViewById<RadioButton>(R.id.finish)

        val avatar = findViewById<ImageView>(R.id.imgAvatar)
        avatar.setOnClickListener {
            Toast.makeText(this, "Добавление картинки", Toast.LENGTH_SHORT).show()
        }

        val projType = intent.getStringExtra("projType")
        val finishRow = findViewById<TableRow>(R.id.finishRow)
        val stitchesBeforeRow = findViewById<TableRow>(R.id.stitchesBeforeRow)
        val startDateRow = findViewById<TableRow>(R.id.startDateRow)
        val planDateRow = findViewById<TableRow>(R.id.planDateRow)

        fun updateVisibility() {
            when {
                presentRBtn.isChecked -> {
                    finishRow.visibility = View.GONE
                    stitchesBeforeRow.visibility = View.VISIBLE
                    startDateRow.visibility = View.VISIBLE
                    planDateRow.visibility = View.VISIBLE
                }
                futureRBtn.isChecked -> {
                    finishRow.visibility = View.GONE
                    stitchesBeforeRow.visibility = View.GONE
                    startDateRow.visibility = View.GONE
                    planDateRow.visibility = View.VISIBLE
                }
                finishRBtn.isChecked -> {
                    finishRow.visibility = View.VISIBLE
                    stitchesBeforeRow.visibility = View.GONE
                    startDateRow.visibility = View.VISIBLE
                    planDateRow.visibility = View.GONE
                }
            }
        }

        when (projType) {
            "present" -> {
                presentRBtn.isChecked = true
                updateVisibility()
            }
            "future" -> {
                futureRBtn.isChecked = true
                updateVisibility()
            }
            "finish" -> {
                finishRBtn.isChecked = true
                updateVisibility()
            }
        }

        presentRBtn.setOnClickListener {
            updateVisibility()
        }

        futureRBtn.setOnClickListener {
            updateVisibility()
        }

        finishRBtn.setOnClickListener {
            updateVisibility()
        }

    }
}
