package com.example.androidcrossstitchcounter.widgets

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.databinding.PassVisibilityBinding

class PassVisWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = PassVisibilityBinding.inflate(LayoutInflater.from(context), this, true)
    var isVisible = false

    init {
        binding.visibilityToggle.setOnClickListener {
            if (!isVisible) {
                binding.visibilityToggle.setImageResource(R.drawable.eye_open)
                binding.passEditTxt.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.visibilityToggle.setImageResource(R.drawable.eye_close)
                binding.passEditTxt.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            isVisible = !isVisible
            binding.passEditTxt.setSelection(binding.passEditTxt.text.length)
        }
    }

    fun getText(): Editable? = binding.passEditTxt.text

    fun setText(text: CharSequence) {
        binding.passEditTxt.setText(text)
    }
}