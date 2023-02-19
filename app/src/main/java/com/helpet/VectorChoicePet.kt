package com.helpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_vector_choice_pet.*

class VectorChoicePet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vector_choice_pet)

        ex1.setOnClickListener {
            val intent= Intent(this, VectorCamera::class.java  )
            startActivity(intent)
        }
    }
}