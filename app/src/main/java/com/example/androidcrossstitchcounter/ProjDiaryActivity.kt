package com.example.androidcrossstitchcounter

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class ProjDiaryActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.proj_diary_activity)

        val aboutProjLayout = findViewById<ConstraintLayout>(R.id.innerLayout)
        val aboutProjText = findViewById<TextView>(R.id.aboutProj)

        aboutProjText.setOnClickListener {
            if (aboutProjLayout.visibility == View.VISIBLE) {
                // Плавное скрытие
                aboutProjLayout.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        aboutProjLayout.visibility = View.GONE
                    }
                    .start()
            } else {
                // Плавное появление
                aboutProjLayout.alpha = 0f
                aboutProjLayout.visibility = View.VISIBLE
                aboutProjLayout.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
        }

        val addRow = findViewById<TableRow>(R.id.addRow)
        val imageAdd = findViewById<ImageView>(R.id.imageAdd)
        val imageCheck = findViewById<ImageView>(R.id.imageCheck)
        val imageCancel = findViewById<ImageView>(R.id.imageCancel)

        fun changeVisibility(isEdit: Boolean) {
            if(isEdit) {
                addRow.visibility = View.VISIBLE
                imageCheck.visibility = View.VISIBLE
                imageCancel.visibility = View.VISIBLE
                imageAdd.visibility = View.GONE
            } else {
                addRow.visibility = View.GONE
                imageCheck.visibility = View.GONE
                imageCancel.visibility = View.GONE
                imageAdd.visibility = View.VISIBLE
            }
        }
        changeVisibility(false)

        imageAdd.setOnClickListener {
            changeVisibility(true)
        }
        imageCheck.setOnClickListener {
            changeVisibility(false)
        }
        imageCancel.setOnClickListener {
            changeVisibility(false)
        }
    }
}
