package com.example.androidcrossstitchcounter

import android.view.View

class Animation {
    companion object {
        fun hiding(obj: View) {
            if (obj.visibility == View.VISIBLE) {
                // Плавное скрытие
                obj.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        obj.visibility = View.GONE
                    }
                    .start()
            } else {
                // Плавное появление
                obj.alpha = 0f
                obj.visibility = View.VISIBLE
                obj.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
        }
    }
}