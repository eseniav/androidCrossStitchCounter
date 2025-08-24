package com.example.androidcrossstitchcounter

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddProjActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_proj_activity)

        val avatar = findViewById<ImageView>(R.id.imgAvatar)
        avatar.setOnClickListener {
            Toast.makeText(this, "Добавление картинки", Toast.LENGTH_SHORT).show()
        }
    }
}
