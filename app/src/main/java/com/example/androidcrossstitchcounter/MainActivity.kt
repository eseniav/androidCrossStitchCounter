package com.example.androidcrossstitchcounter

import UserDao
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    fun authUser(login: String, pass: String, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUserByLogin(login)
            val success = user?.password == pass
            withContext(Dispatchers.Main) {
                onResult(success)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.auth_activity)

        val loginBtn = findViewById<Button>(R.id.enterBtn)
        val loginBox = findViewById<EditText>(R.id.loginTxtBox)
        val passWidget = findViewById<PassVisWidget>(R.id.pWid)
        val db = DataBaseProvider.getDB(this)
        userDao = db.userDao()

        loginBtn.setOnClickListener {
            val userName = loginBox.text.toString()
            val password = passWidget.getText().toString()
            authUser(userName, password) { success ->
                if(success) {
                    Toast.makeText(this, "Добро пожаловать, $userName!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, ProjActivity::class.java)
                    intent.putExtra("LOGIN", userName)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Проверьте правильность вводимых данных!", Toast.LENGTH_SHORT).show()
                }
            }
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
