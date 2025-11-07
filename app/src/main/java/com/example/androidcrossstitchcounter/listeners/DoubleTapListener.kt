package com.example.androidcrossstitchcounter.listeners

import android.view.MotionEvent
import android.view.View

class DoubleTapListener(
    private val position: Int,
    private val onDoubleTap: (Int) -> Unit
) : View.OnTouchListener {

    companion object {
        private const val DOUBLE_TAP_TIMEOUT = 300L // мс между кликами
    }

    private var lastClickTime: Long = 0

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime < DOUBLE_TAP_TIMEOUT) {
                onDoubleTap(position)
                lastClickTime = 0 // сброс, чтобы не срабатывало повторно
                return true
            }

            lastClickTime = currentTime
        }
        return false
    }
}
