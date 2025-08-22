package com.example.androidcrossstitchcounter

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView

class PassVisWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val visToggle: ImageView
    private val passEdit: EditText

    var isVisible = false

    init {
        LayoutInflater.from(context).inflate(R.layout.pass_visibility, this, true)

        visToggle = findViewById<ImageView>(R.id.visibilityToggle)
        passEdit = findViewById<EditText>(R.id.passEditTxt)

        visToggle.setOnClickListener {
            if (!isVisible) {
                visToggle.setImageResource(R.drawable.eye_open)
                passEdit.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                visToggle.setImageResource(R.drawable.eye_close)
                passEdit.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            isVisible = !isVisible
            passEdit.setSelection(passEdit.text.length)
        }
    }

    fun getText(): Editable? = passEdit.text

    fun setText(text: CharSequence) {
        passEdit.setText(text)
    }
}
