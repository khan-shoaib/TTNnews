package com.example.ttnnews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ttnnews.category.CategoryFrag

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val categoryFrag = CategoryFrag()
        supportFragmentManager.beginTransaction().add(R.id.container, categoryFrag).commit()
    }
}