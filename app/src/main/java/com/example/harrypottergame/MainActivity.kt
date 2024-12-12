package com.example.harrypottergame

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import logic.GameManager
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var main_BTN_leftArrow: FloatingActionButton
    private lateinit var main_BTN_rightArrow: FloatingActionButton
    private lateinit var hearts: Array<AppCompatImageView>
    private lateinit var gameManager: GameManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        findViews()
        gameManager = GameManager(hearts = hearts)
        gameManager.startGame()
    }

    private fun initViews() {
        main_BTN_leftArrow.setOnClickListener{gameManager.moveHarry(-1)}
        main_BTN_rightArrow.setOnClickListener{gameManager.moveHarry(1)}
    }


    private fun findViews() {
        main_BTN_leftArrow = findViewById(R.id.main_BTN_leftArrow)
        main_BTN_rightArrow = findViewById(R.id.main_BTN_rightArrow)

        hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )
    }
}









