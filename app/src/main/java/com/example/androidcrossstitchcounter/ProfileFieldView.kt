package com.example.androidcrossstitchcounter

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
    private var profileValue: TextView
    private val imgEdit: ImageView
    private val imgCheck: ImageView
    private val profileEditText: EditText
    private val imgCancel: ImageView
    private val label: TextView
    private var valueText = ""
    private var onValueChangeListener: ((String) -> Unit)? = null
    var onSaveValue: ((newValue: String) -> Unit)? = null
    var onEdit: ((isEdit: Boolean) -> Unit)? = null

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_profile_field, this, true)


        profileValue = findViewById<TextView>(R.id.value)
        imgEdit = findViewById<ImageView>(R.id.imageEdit)
        imgCheck = findViewById<ImageView>(R.id.imageCheck)
        profileEditText = findViewById<EditText>(R.id.edit)
        imgCancel = findViewById<ImageView>(R.id.imageCancel)
        label = findViewById<TextView>(R.id.label)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProfileFieldView, defStyleAttr, 0)
        val labelTxt = typedArray.getString(R.styleable.ProfileFieldView_label) ?: ""
        val valueTxt = typedArray.getString(R.styleable.ProfileFieldView_value) ?: ""
        val inputType = typedArray.getInt(R.styleable.ProfileFieldView_inputType, InputType.TYPE_CLASS_TEXT)
        setLabel(labelTxt)
        setValue(valueTxt)
        setInputType(inputType)
        typedArray.recycle()

        setupListeners()
        profileEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                onValueChangeListener?.invoke(s.toString())
            }
        })
    }

    fun setupListeners() {
        imgEdit.setOnClickListener {
            setValue(valueText)
            changeVisibility()
            onEdit?.invoke(true)
        }
        imgCheck.setOnClickListener {
            valueText = getValue()
            setValue(valueText)
            onSaveValue?.invoke(valueText)
            changeVisibility()
            onEdit?.invoke(false)
        }
        imgCancel.setOnClickListener {
            setValue(valueText)
            changeVisibility()
            onEdit?.invoke(false)
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
        valueText = s
        profileValue.text = s
        profileEditText.setText(s)
    }

    fun setLabel(s: String) {
        label.text = s
    }

    fun setInputType(type: Int) {
        profileEditText.inputType = type
    }

    fun setOnValueChangeListener(listener: (String) -> Unit) {
        onValueChangeListener = listener
    }

    fun setTxtWatcher(watcher: TextWatcher) {
        profileEditText.addTextChangedListener(watcher)
    }

    fun setError(errorText: String) {
        profileEditText.error = errorText
    }
}
