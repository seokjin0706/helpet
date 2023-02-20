package com.helpet.login

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.helpet.R

class Find_idpw : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.find)

        val btn_find_id : Button = findViewById(R.id.btn_find_id)
        val btn_find_pw : Button = findViewById(R.id.btn_find_pw)


        setFrag(0)

        btn_find_id.setOnClickListener{
            setFrag(0)
        }

        btn_find_pw.setOnClickListener{
            setFrag(1)
        }


    }

    private fun setFrag(fragNum : Int) {
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum)
        {
            0-> {
                ft.replace(R.id.find_mainframe, Frag1_find_id()).commit()
            }
            1-> {
                ft.replace(R.id.find_mainframe, Frag2_find_pw()).commit()
            }
        }

    }
}