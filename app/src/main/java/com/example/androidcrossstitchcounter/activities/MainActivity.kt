package com.example.androidcrossstitchcounter.activities

import User
import UserDao
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.databinding.MainActivityBinding
import com.example.androidcrossstitchcounter.fragments.ProfileFragment
import com.example.androidcrossstitchcounter.fragments.ProjFragment
import com.example.androidcrossstitchcounter.fragments.SettingsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: MainActivityBinding
    private lateinit var userDao: UserDao
    private val proj = ProjFragment()
    private val profile = ProfileFragment()
    private val settings = SettingsFragment()
    private val app: App by lazy {
        application as App
    }

    fun checkUser(id: Int, onResult: (User?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUserById(id)
            withContext(Dispatchers.Main) {
                onResult(user)
            }
        }
    }

    fun redirect(page: String) {
        val intent = Intent(this@MainActivity,
            if(page == "Auth") AuthActivity::class.java else ProjActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun handleAuth() {
        val sharedPreferences = getSharedPreferences("app_data", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        if(userId == -1) {
            redirect("Auth")
        } else {
            checkUser(userId) { user ->
              if(user != null) {
                  app.user = user
                  supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.frame, proj)
                      .commit()

              } else
                  redirect("Auth")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val db = DataBaseProvider.getDB(this)
        userDao = db.userDao()

        handleAuth()

        fun setColor(element: ImageView) {
            val color = ContextCompat.getColor(this, R.color.dark_plum)
            element.setColorFilter(color)
        }

        binding.projectList.setOnClickListener {
            setColor(binding.projectList)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, proj)
                .commit()
        }
        binding.profile.setOnClickListener {
            setColor(binding.profile)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, profile)
                .commit()
        }
        binding.settings.setOnClickListener {
            setColor(binding.settings)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, settings)
                .commit()
        }
        binding.logout.setOnClickListener {
            setColor(binding.logout)
            redirect("Auth")
        }
    }
}
