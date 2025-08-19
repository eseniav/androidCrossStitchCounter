package com.example.androidcrossstitchcounter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_activity)
        val registrBtn = findViewById<Button>(R.id.regBtnRegAct)
        val login = findViewById<EditText>(R.id.etxtLog)
        registrBtn.setOnClickListener {
            if(login.text.isNotEmpty()) {
                Toast.makeText(this, "Регистрация успешно выполнена!",
                    Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Проверьте правильность заполнения формы!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}