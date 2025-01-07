package com.example.harrypottergame

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import interfaces.TiltCallback
import logic.GameManager
import utilities.TiltDetector

class MainActivity : AppCompatActivity(), TiltCallback {

    private lateinit var main_BTN_leftArrow: FloatingActionButton
    private lateinit var main_BTN_rightArrow: FloatingActionButton
    private lateinit var hearts: Array<AppCompatImageView>
    private lateinit var gameManager: GameManager
    private lateinit var gridLayout: GridLayout

    private var tiltDetector: TiltDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mode = intent.getStringExtra("mode") ?: "arrow"
        val speed = intent.getStringExtra("speed") ?: "slow"

        findViews()
        initViews()


        gameManager = GameManager(gridLayout, hearts, this)
        if (mode == "arrow") {
            gameManager.setSpeed(if (speed == "fast") 500 else 800)
            gameManager.startGame()
        } else if (mode == "sensor") {
            main_BTN_rightArrow.visibility = View.GONE
            main_BTN_leftArrow.visibility = View.GONE

            // if the selected mode is sensor mode, the sensor detection begin working
            tiltDetector = TiltDetector(this,this)
            tiltDetector?.start()

        }
    }

    override fun tiltRight() {
        if (gameManager.harryLane < 4) {
            gameManager.moveHarry(gameManager.harryLane + 1)
        }
    }

    override fun tiltLeft() {
        if (gameManager.harryLane > 0) {
            gameManager.moveHarry(gameManager.harryLane - 1)
        }
    }

    override fun tiltCenter() {
        gameManager.moveHarry(2) // Move Harry to the center lane
    }


    override fun onDestroy() {
        super.onDestroy()
        tiltDetector?.stop()
    }


    private fun findViews() {
        main_BTN_leftArrow = findViewById(R.id.main_BTN_leftArrow)
        main_BTN_rightArrow = findViewById(R.id.main_BTN_rightArrow)
        hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )
        gridLayout = findViewById(R.id.main_grid)
    }

    private fun initViews() {
        main_BTN_leftArrow.setOnClickListener {
            gameManager.moveHarry(-1) // Move Harry left
        }
        main_BTN_rightArrow.setOnClickListener {
            gameManager.moveHarry(1) // Move Harry right
        }
    }
}
