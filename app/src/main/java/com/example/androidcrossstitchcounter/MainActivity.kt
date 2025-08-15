package com.example.androidcrossstitchcounter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.auth_activity)

        val loginBtn = findViewById<Button>(R.id.enterBtn)
        val loginBox = findViewById<EditText>(R.id.loginTxtBox)

        loginBtn.setOnClickListener {
            val userName = loginBox.text.toString()
            Toast.makeText(this, "Добро пожаловать, $userName!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, ProjActivity::class.java)
            startActivity(intent)
        }
        val regBtn = findViewById<Button>(R.id.regBtn)
        regBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, RegActivity::class.java)
            startActivity(intent)
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}
