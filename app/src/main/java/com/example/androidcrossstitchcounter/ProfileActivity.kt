package com.example.androidcrossstitchcounter

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        val avatar = findViewById<ImageView>(R.id.imgAvatar)

        avatar.setOnClickListener {
            Toast.makeText(this, "Изменение картинки", Toast.LENGTH_SHORT).show()
        }

        val surnameWidget = findViewById<ProfileFieldView>(R.id.surnameRow)
        surnameWidget.setValue("Иванова")

        val nameWidget = findViewById<ProfileFieldView>(R.id.nameRow)
        nameWidget.setLabel("Имя")

        val patrWidget = findViewById<ProfileFieldView>(R.id.patrRow)
        patrWidget.setLabel("Отчество")

        val logWidget = findViewById<ProfileFieldView>(R.id.logRow)
        logWidget.setLabel("Логин")

        val passWidget = findViewById<ProfileFieldView>(R.id.passRow)
        passWidget.setLabel("Пароль")
        passWidget.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

        val repeatPassWidget = findViewById<ProfileFieldView>(R.id.repeatPassRow)
        repeatPassWidget.setLabel("Повторите пароль")

        val phoneWidget = findViewById<ProfileFieldView>(R.id.phoneRow)
        phoneWidget.setLabel("Телефон")

        val emailWidget = findViewById<ProfileFieldView>(R.id.emailRow)
        emailWidget.setLabel("Email")

        val birthDateWidget = findViewById<ProfileFieldView>(R.id.birthDateRow)
        birthDateWidget.setLabel("Дата рождения")
    }
}
