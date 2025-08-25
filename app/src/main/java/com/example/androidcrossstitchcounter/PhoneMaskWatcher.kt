package com.example.androidcrossstitchcounter

import android.text.Editable
import android.text.TextWatcher

class PhoneMaskWatcher: TextWatcher {
    private val mask = "(###) ###-##-##"
    private val prefix = "+7 "
    private var isUpdating = false

    private fun unmask(s: String) = s.replace(Regex("[^\\d]"), "")

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
        if(isUpdating)
            return
        var digits = unmask(s.toString())
        if(digits.startsWith("7"))
            digits = digits.substring(1)
        var formatted = prefix
        var digitIndex = 0

        for(ch in mask) {
            if (ch != '#') {
                formatted += ch
                continue
            }
            if(digitIndex >= digits.length)
                break
            formatted += digits[digitIndex]
            ++ digitIndex
        }

        isUpdating = true
        (s as Editable).replace(0, s.length, formatted)
        isUpdating = false
    }
}
