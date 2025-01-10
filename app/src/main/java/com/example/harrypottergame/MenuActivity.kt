package com.example.harrypottergame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_BTN_sensorMode: Button
    private lateinit var menu_BTN_arrowMode: Button
    private lateinit var menu_BTN_slow: Button
    private lateinit var menu_BTN_fast: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()
    }

    private fun findViews() {
        menu_BTN_sensorMode = findViewById(R.id.menu_BTN_sensorMode)
        menu_BTN_arrowMode = findViewById(R.id.menu_BTN_arrowMode)
        menu_BTN_slow = findViewById(R.id.menu_BTN_slow)
        menu_BTN_fast = findViewById(R.id.menu_BTN_fast)
    }

    private fun initViews() {
        menu_BTN_slow.visibility = View.GONE
        menu_BTN_fast.visibility = View.GONE

        menu_BTN_arrowMode.setOnClickListener{
            menu_BTN_slow.visibility = View.VISIBLE
            menu_BTN_fast.visibility = View.VISIBLE
        }

        menu_BTN_sensorMode.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "sensor")
            startActivity(intent)
        }

        menu_BTN_slow.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "arrow")
            intent.putExtra("speed", "slow")
            startActivity(intent)
        }

        menu_BTN_fast.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "arrow")
            intent.putExtra("speed", "fast")
            startActivity(intent)
        }
    }




}