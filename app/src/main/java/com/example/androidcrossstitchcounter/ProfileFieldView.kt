package com.example.androidcrossstitchcounter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.transition.Visibility
import androidx.viewpager2.widget.ViewPager2

class ProfileFieldView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private val profileValue: TextView
    private val imgEdit: ImageView
    private val imgCheck: ImageView
    private val profileEditText: EditText
    private val imgCancel: ImageView

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_profile_field, this, true)

        profileValue = findViewById<TextView>(R.id.value)
        imgEdit = findViewById<ImageView>(R.id.imageEdit)
        imgCheck = findViewById<ImageView>(R.id.imageCheck)
        profileEditText = findViewById<EditText>(R.id.edit)
        imgCancel = findViewById<ImageView>(R.id.imageCancel)

        setupListeners()
    }

    fun setupListeners() {
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

    private fun View.setVisible(isVisible: Boolean) {
        visibility = if(isVisible) VISIBLE else GONE
    }
    fun changeVisibility(isEdit: Boolean) {
            imgCheck.setVisible(isEdit)
            profileEditText.setVisible(isEdit)
            imgCancel.setVisible(isEdit)
            profileValue.setVisible(!isEdit)
            imgEdit.setVisible(!isEdit)
    }
}
