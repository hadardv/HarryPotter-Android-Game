package com.example.harrypottergame

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
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
    private lateinit var main_TXT_score: TextView


    private var tiltDetector: TiltDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("Unhandled Exception", "Exception in thread: ${thread.name}", throwable)
        }

        findViews()
        initViews()

        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            startGameBasedOnMode()
        }
    }


    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startGameBasedOnMode()
        } else {
            Toast.makeText(this, "Location permission denied! The game will continue without location data.", Toast.LENGTH_SHORT).show()
        }
    }



    private fun startGameBasedOnMode() {
        val mode = intent.getStringExtra("mode") ?: "arrow"
        val speed = intent.getStringExtra("speed") ?: "slow"

        gameManager = GameManager(gridLayout, hearts, main_TXT_score, this)
        if (mode == "arrow") {
            gameManager.setSpeed(if (speed == "fast") 500 else 800)
            gameManager.startGame()
        } else if (mode == "sensor") {
            main_BTN_rightArrow.visibility = View.GONE
            main_BTN_leftArrow.visibility = View.GONE

            tiltDetector = TiltDetector(this, this)
            tiltDetector?.start()
            gameManager.startGame()
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

    private fun findViews() {
        main_BTN_leftArrow = findViewById(R.id.main_BTN_leftArrow)
        main_BTN_rightArrow = findViewById(R.id.main_BTN_rightArrow)
        hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )
        gridLayout = findViewById(R.id.main_grid)
        main_TXT_score = findViewById(R.id.main_TXT_score)
    }

    private fun initViews() {
        main_BTN_leftArrow.setOnClickListener {
            if (gameManager.harryLane > 0) {
                gameManager.moveHarry(gameManager.harryLane - 1) // Move Harry left
            }
        }
        main_BTN_rightArrow.setOnClickListener {
            if (gameManager.harryLane < 4) {
                gameManager.moveHarry(gameManager.harryLane + 1) // Move Harry right
            }
        }
    }

    override fun onStop() {
        super.onStop()
        gameManager.stopGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        gameManager.stopGame()
    }
}
