package com.example.androidcrossstitchcounter.activities

import User
import UserDao
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.services.Validation
import com.example.androidcrossstitchcounter.watchers.PhoneMaskWatcher
import com.example.androidcrossstitchcounter.widgets.ProfileFieldView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity: AppCompatActivity()  {
    private lateinit var userDao: UserDao
    private lateinit var user: User
    private val app: App by lazy {
        application as App
    }

    private fun updateUserProp(propName: String, propValue: String) {
        when(propName) {
            "phoneNumber" -> user.phoneNumber = propValue
            "email" -> user.email = propValue
            "password" -> user.password = propValue
        }
        CoroutineScope(Dispatchers.IO).launch {
            userDao.updateUser(user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        user = app.user!!
        val avatar = findViewById<ImageView>(R.id.imgAvatar)
        val db = DataBaseProvider.getDB(this)
        userDao = db.userDao()
        var passRepeat = findViewById<LinearLayout>(R.id.passRepeat)
        val repeatPassRow = findViewById<EditText>(R.id.repeatPassRow)

        avatar.setOnClickListener {
            Toast.makeText(this, "Изменение картинки", Toast.LENGTH_SHORT).show()
        }

        val surnameWidget = findViewById<ProfileFieldView>(R.id.surnameRow)
        //surnameWidget.setValue()

        val nameWidget = findViewById<ProfileFieldView>(R.id.nameRow)
        nameWidget.setLabel("Имя")

        val patrWidget = findViewById<ProfileFieldView>(R.id.patrRow)
        patrWidget.setLabel("Отчество")

        val logWidget = findViewById<ProfileFieldView>(R.id.logRow)
        logWidget.setLabel("Логин")
        logWidget.setValue(user.login)

        val passWidget = findViewById<ProfileFieldView>(R.id.passRow)
        passWidget.setLabel("Пароль")
        passWidget.setValue(user.password)
        passWidget.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        passWidget.onSaveValue = fun(newValue) {
            passWidget.clearError()
            if (!Validation.Companion.checkPassword(newValue)) {
                passWidget.setError("Пароль должен соответствовать критериям сложности")
                return
            }
            if (!Validation.Companion.checkMatch(newValue, repeatPassRow.text.toString())) {
                passWidget.setError("Пароли должны совпадать")
                return
            }
            updateUserProp("password", newValue)
        }

        passWidget.onEdit = { isEdit ->
            passRepeat.visibility = if(isEdit) View.VISIBLE else View.GONE
        }

        val phoneWidget = findViewById<ProfileFieldView>(R.id.phoneRow)
        phoneWidget.setLabel("Телефон")
        phoneWidget.setTxtWatcher(PhoneMaskWatcher())
        phoneWidget.setValue(user.phoneNumber)
        phoneWidget.onSaveValue = { newValue ->
            updateUserProp("phoneNumber", newValue)
        }

        val emailWidget = findViewById<ProfileFieldView>(R.id.emailRow)
        emailWidget.setLabel("Email")
        emailWidget.setValue(user.email)
        emailWidget.onSaveValue = { newValue ->
            updateUserProp("email", newValue)
        }

        val birthDateWidget = findViewById<ProfileFieldView>(R.id.birthDateRow)
        birthDateWidget.setLabel("Дата рождения")
    }
}