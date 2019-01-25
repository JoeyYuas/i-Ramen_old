package com.ramenticket.ramenticket

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_my_ramen_point.*

class myRamenPointActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ramen_point)

        val spinner = findViewById<Spinner>(R.id.spinner)


        var howManyPoint = 1
        val requestCode = 1000
        val intent = Intent(this, TopCalendar::class.java)
        intent.putExtra("howManyPoint", howManyPoint)

        val optionButton = findViewById<Button>(R.id.setOption)
        optionButton.setOnClickListener{

            val yourChoice = spinner.selectedItemPosition

            when(yourChoice){
                0 -> {
                    howManyPoint = 1
                }
                1 -> {
                    howManyPoint = 2
                }
                2 -> {
                    howManyPoint = 3
                }
                3 -> {
                    howManyPoint = 4
                }
                4 -> {
                    Toast.makeText(this, "食べ過ぎです！もっと少なく！", Toast.LENGTH_LONG).show()
                }
            }

            if(yourChoice != 4) {
                intent.putExtra("howManyPoint", howManyPoint)
                startActivityForResult(intent, requestCode)
            }
        }
    }
}
