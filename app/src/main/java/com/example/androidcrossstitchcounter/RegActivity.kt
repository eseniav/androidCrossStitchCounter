package com.example.androidcrossstitchcounter

import User
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_activity)

        val db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "app_db").build()
        val userDao = db.userDao()

        val registrBtn = findViewById<Button>(R.id.regBtnRegAct)
        val login = findViewById<EditText>(R.id.etxtLog)
        val password = findViewById<EditText>(R.id.etxtPass)
        val repeatPass = findViewById<EditText>(R.id.etxtPassRep)
        val email = findViewById<EditText>(R.id.etxtEmail)
        val phone = findViewById<EditText>(R.id.etxtPhone)

        phone.addTextChangedListener(PhoneMaskWatcher())

        phone.addTextChangedListener(object: TextWatcher{
            private val phonePattern = Regex("""^(\+7|7|8)?[\s\-]?\(?[0-9]{3}\)?[\s\-]?[0-9]{3}[\s\-]?[0-9]{2}[\s\-]?[0-9]{2}$""")
            fun checkPhone(input: String) = phonePattern.matches(input)
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if(!checkPhone(input)) {
                    phone.error = "Проверьте правильность вводимых данных!"
                } else {
                    phone.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        val date = findViewById<EditText>(R.id.etxtBDate)
        date.isFocusable = false
        date.isClickable = true
        date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerdialog = DatePickerDialog(this, {_, selectedYear, selectedMonth, selectedDay ->
                val formatedDate = String.format("%02d.%02d.%04d", selectedDay, selectedMonth + 1, selectedYear)
                date.setText(formatedDate)
            }, year, month, day)
            datePickerdialog.show()
        }

        registrBtn.setOnClickListener {
            var isValid = true

            val loginText = login.text.toString()
            if(loginText.isBlank()) {
                isValid = false
                login.error = "Поле Логин должно быть заполнено!"
            } else if (loginText.length < 3 || loginText.length > 30) {
                isValid = false
                login.error = "Логин должен быть от 3 до 30 символов!"
            } else if (loginText.any { it.isWhitespace() }) {
                isValid = false
                login.error = "Логин не должен содержать пробелов!"
            }

            val passwordText = password.text.toString()
            val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,30}\$"
            if(passwordText.isBlank()) {
                isValid = false
                password.error = "Поле Пароль должно быть заполнено!"
            } else if (!passwordText.matches(passwordPattern.toRegex())) {
                isValid = false
                password.error = "Пароль должен быть без пробелов и содержать от 8 до 30 символов, цифру, буквы в обоих регистрах и спецсимвол!"
            } else if (repeatPass.text.toString() != passwordText) {
                isValid = false
                repeatPass.error = "Пароли должны совпадать!"
            }

            val emailText = email.text.toString()
            if(emailText.isBlank()) {
                isValid = false
                email.error = "Поле Email должно быть заполнено!"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                isValid = false
                email.error = "Проверьте правильность заполнения поля!"
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
