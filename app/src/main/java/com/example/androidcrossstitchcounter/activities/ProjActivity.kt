package com.example.androidcrossstitchcounter.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrossstitchcounter.App
import com.example.androidcrossstitchcounter.R
import com.example.androidcrossstitchcounter.services.Animation

class ProjActivity : AppCompatActivity() {
    private val app: App by lazy {
        application as App
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proj)

        val login = app.user!!.login
        val loginVal = findViewById<TextView>(R.id.logProj)
        loginVal.text = login

        loginVal.setOnClickListener {
            val intent = Intent(this@ProjActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        val projName = findViewById<TextView>(R.id.projName)
        projName.setOnClickListener {
            val intent = Intent(this@ProjActivity, ProjDiaryActivity::class.java)
            startActivity(intent)
        }

        val addPresent = findViewById<ImageView>(R.id.imageAdd)
        val addFuture = findViewById<ImageView>(R.id.imageAddF)
        val addFinish = findViewById<ImageView>(R.id.imageAddFinish)
        addPresent.setOnClickListener {
            val intent = Intent(this@ProjActivity, AddProjActivity::class.java)
            intent.putExtra("projType", "present")
            startActivity(intent)
        }
        addFuture.setOnClickListener {
            val intent = Intent(this@ProjActivity, AddProjActivity::class.java)
            intent.putExtra("projType", "future")
            startActivity(intent)
        }
        addFinish.setOnClickListener {
            val intent = Intent(this@ProjActivity, AddProjActivity::class.java)
            intent.putExtra("projType", "finish")
            startActivity(intent)
        }

        val presentTxt = findViewById<TextView>(R.id.present)
        val presentInner = findViewById<TableLayout>(R.id.currentProj)
        presentTxt.setOnClickListener {
            Animation.Companion.hiding(presentInner)
        }

        val futureTxt = findViewById<TextView>(R.id.future)
        val futureInner = findViewById<TableLayout>(R.id.projTable)
        futureTxt.setOnClickListener {
            Animation.Companion.hiding(futureInner)
        }

        val finishTxt = findViewById<TextView>(R.id.finish)
        val finishInner = findViewById<TableLayout>(R.id.finishProjTable)
        finishTxt.setOnClickListener {
            Animation.Companion.hiding(finishInner)
        }
//        val projects = listOf(
//            listOf("Пума", "102x302", "12.03.2025", "05.05.2025", "2500"),
//            listOf("Nike", "Air Max 90", "15.04.2025", "20.06.2025", "8990"),
//            listOf("Adidas", "Ultraboost 21", "01.05.2025", "10.07.2025", "12990"),
//            listOf("Reebok", "Classic Leather", "22.03.2025", "30.05.2025", "5990"),
//            listOf("Puma", "RS-X", "10.04.2025", "15.06.2025", "7990"),
//            listOf("New Balance", "574", "05.05.2025", "25.07.2025", "7490"),
//            listOf("Asics", "Gel-Lyte III", "18.03.2025", "12.05.2025", "8990"),
//            listOf("Vans", "Old Skool", "03.04.2025", "08.06.2025", "5490"),
//            listOf("Converse", "Chuck Taylor", "12.05.2025", "22.07.2025", "4590"),
//            listOf("Under Armour", "HOVR Phantom", "25.04.2025", "05.07.2025", "10990"),
//            listOf("Salomon", "XT-6", "08.05.2025", "18.07.2025", "11990"),
//        )
//        val table = findViewById<TableLayout>(R.id.projTable)
//        projects.forEach { data ->
//            val row1 = TableRow(this)
//            val textView = TextView(this)
//            textView.text = data[0]
//            textView.layout
//            row1.addView(textView)
//            table.addView(row1)
//            val row2 = TableRow(this)
//        }
    }
}