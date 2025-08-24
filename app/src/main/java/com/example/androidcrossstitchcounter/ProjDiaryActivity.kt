package com.example.androidcrossstitchcounter

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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
    }
}
