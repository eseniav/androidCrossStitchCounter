package com.example.androidcrossstitchcounter.activities

import User
import UserDao
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.widgets.PassVisWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private val app: App by lazy {
        application as App
    }
    fun authUser(login: String, pass: String, onResult: (User?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = userDao.getUserByLogin(login)
            val success = user?.password == pass
            if(!success)
                user = null
            withContext(Dispatchers.Main) {
                onResult(user)
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
            authUser(userName, password) { user ->
                if(user != null) {
                    app.user = user
                    Toast.makeText(this, "Добро пожаловать, ${app.user!!.login}!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, ProjActivity::class.java)
                    startActivity(intent)
                    finish()
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