package com.example.androidcrossstitchcounter.activities

import UserDao
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: MainActivityBinding
    private lateinit var userDao: UserDao
    private val app: App by lazy {
        application as App
    }

    fun redirect() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val db = DataBaseProvider.getDB(this)
        userDao = db.userDao()

        redirect()
    }
}
