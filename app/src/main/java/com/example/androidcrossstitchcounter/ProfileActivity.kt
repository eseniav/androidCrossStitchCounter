package com.example.androidcrossstitchcounter

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        val avatar = findViewById<ImageView>(R.id.imgAvatar)

        avatar.setOnClickListener {
            Toast.makeText(this, "Изменение картинки", Toast.LENGTH_SHORT).show()
        }
    }
}
