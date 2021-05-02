package com.example.ttnnews

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ttnnews.fav.FavActivity

class WelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)
        val btn_news = findViewById<Button>(R.id.btn_news)

        btn_news.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            // start your news activity
            startActivity(intent)
        }
        val btn_savednews = findViewById<Button>(R.id.btn_savednews)
        btn_savednews.setOnClickListener {
            val intent = Intent(this, FavActivity::class.java)
            // start your savednews activity
            startActivity(intent)


        }


    }
}
