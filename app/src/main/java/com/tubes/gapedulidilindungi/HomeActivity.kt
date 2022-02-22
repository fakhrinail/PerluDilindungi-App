package com.tubes.gapedulidilindungi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val button = findViewById<Button>(R.id.buttonHome__goToNews)
        button.setOnClickListener{
            val intent = Intent(this@HomeActivity, NewsActivity::class.java)
            startActivity(intent)
        }
    }
}