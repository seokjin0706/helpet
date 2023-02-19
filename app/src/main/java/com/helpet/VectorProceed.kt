package com.helpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class VectorProceed : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vector_proceed)


     Handler().postDelayed({
           val intent= Intent(this,VectorResult::class.java)
                           startActivity(intent)}, 2000L)
    }
}

//startActivity(Intent(StartActivity::class.java, MainActivity::class.java))