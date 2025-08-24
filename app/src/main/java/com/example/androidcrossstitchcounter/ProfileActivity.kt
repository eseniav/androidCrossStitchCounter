package com.example.androidcrossstitchcounter

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        val avatar = findViewById<ImageView>(R.id.imgAvatar)
        val profileValue = findViewById<TextView>(R.id.value)
        val imgEdit = findViewById<ImageView>(R.id.imageEdit)
        val imgCheck = findViewById<ImageView>(R.id.imageCheck)
        val profileEditText = findViewById<EditText>(R.id.edit)
        val imgCancel = findViewById<ImageView>(R.id.imageCancel)

        fun changeVisibility(isEdit: Boolean) {
            if(isEdit) {
                imgCheck.visibility = View.VISIBLE
                profileEditText.visibility = View.VISIBLE
                imgCancel.visibility = View.VISIBLE
                profileValue.visibility = View.GONE
                imgEdit.visibility = View.GONE
            } else {
                imgCheck.visibility = View.GONE
                profileEditText.visibility = View.GONE
                imgCancel.visibility = View.GONE
                profileValue.visibility = View.VISIBLE
                imgEdit.visibility = View.VISIBLE
            }
        }
        changeVisibility(false)
        avatar.setOnClickListener {
            Toast.makeText(this, "Изменение картинки", Toast.LENGTH_SHORT).show()
        }

        imgEdit.setOnClickListener {
            changeVisibility(true)
        }
        imgCheck.setOnClickListener {
            changeVisibility(false)
        }
        imgCancel.setOnClickListener {
            changeVisibility(false)
        }
    }
}
