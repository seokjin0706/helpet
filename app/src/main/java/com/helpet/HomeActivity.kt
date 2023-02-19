package com.helpet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.home.*

class HomeActivity : AppCompatActivity() {

    //lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    R.id.home -> {
                        val mainVectorFragment = VectorMain()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_container, mainVectorFragment).commit()
                    }
                    R.id.calender -> {
                        val mainCalenderFragment = MainCalenderFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_container, mainCalenderFragment).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }
}



