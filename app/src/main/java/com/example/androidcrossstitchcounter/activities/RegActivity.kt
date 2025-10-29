package com.example.androidcrossstitchcounter.activities

import com.example.androidcrossstitchcounter.models.User
import com.example.androidcrossstitchcounter.models.UserDao
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.databinding.RegActivityBinding
import com.example.androidcrossstitchcounter.watchers.PhoneMaskWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.androidcrossstitchcounter.models.DataBaseProvider
import com.example.androidcrossstitchcounter.services.CalendarUtils

class RegActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private  lateinit var binding: RegActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_activity)

        binding = RegActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = DataBaseProvider.getDB(this)
        userDao = db.userDao()
        binding.etxtPhone.addTextChangedListener(PhoneMaskWatcher())

        binding.etxtPhone.addTextChangedListener(object: TextWatcher {
            private val phonePattern = Regex("""^(\+7|7|8)?[\s\-]?\(?[0-9]{3}\)?[\s\-]?[0-9]{3}[\s\-]?[0-9]{2}[\s\-]?[0-9]{2}$""")
            fun checkPhone(input: String) = phonePattern.matches(input)
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if(!checkPhone(input)) {
                    binding.etxtPhone.error = "Проверьте правильность вводимых данных!"
                } else {
                    binding.etxtPhone.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        val date = findViewById<EditText>(R.id.etxtBDate)
        date.isFocusable = false
        date.isClickable = true
        date.setOnClickListener {
            CalendarUtils.setDisplayCalendar(this, date).show()
        }

        binding.regBtnRegAct.setOnClickListener {
            var isValid = true

            val loginText = binding.etxtLog.text.toString()
            if(loginText.isBlank()) {
                isValid = false
                binding.etxtLog.error = "Поле Логин должно быть заполнено!"
            } else if (loginText.length < 3 || loginText.length > 30) {
                isValid = false
                binding.etxtLog.error = "Логин должен быть от 3 до 30 символов!"
            } else if (loginText.any { it.isWhitespace() }) {
                isValid = false
                binding.etxtLog.error = "Логин не должен содержать пробелов!"
            }

            val passwordText = binding.etxtPass.text.toString()
            val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,30}\$"
            if(passwordText.isBlank()) {
                isValid = false
                binding.etxtPass.error = "Поле Пароль должно быть заполнено!"
            } else if (!passwordText.matches(passwordPattern.toRegex())) {
                isValid = false
                binding.etxtPass.error = "Пароль должен быть без пробелов и содержать от 8 до 30 символов, цифру, буквы в обоих регистрах и спецсимвол!"
            } else if (binding.etxtPassRep.text.toString() != passwordText) {
                isValid = false
                binding.etxtPassRep.error = "Пароли должны совпадать!"
            }

            val emailText = binding.etxtEmail.text.toString()
            if(emailText.isBlank()) {
                isValid = false
                binding.etxtEmail.error = "Поле Email должно быть заполнено!"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                isValid = false
                binding.etxtEmail.error = "Проверьте правильность заполнения поля!"
            }

            if(isValid) {
                val user = User(
                    login = loginText,
                    password = passwordText,
                    email = emailText
                    )
                CoroutineScope(Dispatchers.IO).launch {
                    userDao.insertUser(user)

                }
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