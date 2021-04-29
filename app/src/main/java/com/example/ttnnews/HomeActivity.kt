package com.example.ttnnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ttnnews.category.CategoryFrag

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val CategoryFrag = CategoryFrag()
        supportFragmentManager.beginTransaction().add(R.id.container, CategoryFrag).commit()


    }
}