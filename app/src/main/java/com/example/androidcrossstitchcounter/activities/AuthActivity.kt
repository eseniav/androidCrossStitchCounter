package com.example.androidcrossstitchcounter.activities

import User
import UserDao
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.databinding.AuthActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity : AppCompatActivity() {
    private  lateinit var binding: AuthActivityBinding
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
    fun handleRememberMe(id: Int) {
        val sharedPreferences = getSharedPreferences("app_data", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            if(binding.rememberMe.isChecked) {
                putInt("user_id", id)
            } else {
                remove("user_id")
            }
            apply()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = AuthActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DataBaseProvider.getDB(this)
        userDao = db.userDao()

        binding.enterBtn.setOnClickListener {
            val userName = binding.loginTxtBox.text.toString()
            val password = binding.pWid.getText().toString()
            authUser(userName, password) { user ->
                if(user != null) {
                    app.user = user
                    handleRememberMe(user.id)
                    Toast.makeText(this, "Добро пожаловать, ${app.user!!.login}!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AuthActivity, ProjActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Проверьте правильность вводимых данных!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.regBtn.setOnClickListener {
            val intent = Intent(this@AuthActivity, RegActivity::class.java)
            startActivity(intent)
        }
    }
}
