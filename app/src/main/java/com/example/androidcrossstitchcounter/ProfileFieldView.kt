package com.example.androidcrossstitchcounter

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.transition.Visibility

class ProfileFieldView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private val profileValue: TextView
    private val imgEdit: ImageView
    private val imgCheck: ImageView
    private val profileEditText: EditText
    private val imgCancel: ImageView
    private val label: TextView

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_profile_field, this, true)

        profileValue = findViewById<TextView>(R.id.value)
        imgEdit = findViewById<ImageView>(R.id.imageEdit)
        imgCheck = findViewById<ImageView>(R.id.imageCheck)
        profileEditText = findViewById<EditText>(R.id.edit)
        imgCancel = findViewById<ImageView>(R.id.imageCancel)
        label = findViewById<TextView>(R.id.label)

        setupListeners()
    }

    fun setupListeners() {
        imgEdit.setOnClickListener {
            changeVisibility()
        }
        imgCheck.setOnClickListener {
            changeVisibility()
        }
        imgCancel.setOnClickListener {
            changeVisibility()
        }
    }

    private fun View.toggleVisibility() {
        visibility = if(visibility == VISIBLE) GONE else VISIBLE
    }
    fun changeVisibility() {
        imgCheck.toggleVisibility()
        profileEditText.toggleVisibility()
        imgCancel.toggleVisibility()
        profileValue.toggleVisibility()
        imgEdit.toggleVisibility()
    }

    fun getValue() = profileEditText.text.toString()

    fun setValue(s: String) {
        profileValue.text = s
        profileEditText.setText(s)
    }

    fun setLabel(s: String) {
        label.text = s
    }
}
