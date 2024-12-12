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
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var main_BTN_leftArrow: FloatingActionButton
    private lateinit var main_BTN_rightArrow: FloatingActionButton
    private lateinit var hearts: Array<AppCompatImageView>
    private lateinit var matrix: Array<Array<AppCompatImageView>>
    private val rows = 8
    private val columns = 3


    private var lives = 3
    private var isGameRunning = true
    private val handler = Handler(Looper.getMainLooper())
    private var harryLane = 1
    private val MAX_VOLDEMORTS = 2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        initViews()
        startGame()
        }

    private fun initViews() {
       main_BTN_leftArrow.setOnClickListener{moveHarry(-1)}
        main_BTN_rightArrow.setOnClickListener{moveHarry(1)}
    }

    private fun moveHarry(i: Int) {
        matrix[7][harryLane].apply {
            visibility = View.INVISIBLE
            setImageResource(0) // Remove image
        }
        harryLane = (harryLane + i).coerceIn(0, columns - 1)
        matrix[7][harryLane].apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.harry)
        }
    }


    private fun findViews() {
        gridLayout = findViewById(R.id.gridLayout)

        matrix = Array(rows) { row ->
            Array(columns) { col ->
                val imageView = AppCompatImageView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                    }
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    visibility = View.INVISIBLE // Default visibility is INVISIBLE

                    // Set Voldemort images for rows 0-6
                    if (row < 7) {
                        setImageResource(R.drawable.lord_voldemort)
                    }
                }
                gridLayout.addView(imageView)
                imageView
            }
        }

        matrix[7][harryLane].apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.harry)
        }

        main_BTN_leftArrow = findViewById(R.id.main_BTN_leftArrow)
        main_BTN_rightArrow = findViewById(R.id.main_BTN_rightArrow)
        hearts = arrayOf(
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2),
            findViewById(R.id.main_IMG_heart3)
        )
    }


    // The problem here is that when voldemort reaches row 7 (harry's row) his picture not disappears
    private fun startGame() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isGameRunning) return

                // Move Voldemorts down (rows 0-6 only)
                for (row in rows-2 downTo  0) { // Stop at row 0
                    for (col in 0 until columns) {

                        if (matrix[row][col].tag == "voldemort") { // Check if the current cell has voldemort
                            matrix[row + 1][col].apply { // if yes, bring voldemort one cell down
                                visibility = View.VISIBLE
                                setImageResource(R.drawable.lord_voldemort)
                                tag = "voldemort" // Carry the tag down
                            }
                            matrix[row][col].apply {
                                visibility = View.INVISIBLE
                                setImageResource(0) // Clear old position
                                tag = null // Clear the tag
                            }

                        }
                    }
                }


                // Clear the top row before adding new Voldemorts
                for (col in 0 until columns) {
                    matrix[0][col].visibility = View.INVISIBLE
                    matrix[0][col].tag = null // Clear any leftover tags
                }

                val voldemortCount = Random.nextInt(0, MAX_VOLDEMORTS) // Random number of Voldemorts up to 2 each time
                val voldemortPositions = (0 until columns).shuffled().take(voldemortCount)

                for (col in voldemortPositions) {
                    matrix[0][col].apply {
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.lord_voldemort)
                        tag = "voldemort"
                    }
                }

                checkCollision()
                handler.postDelayed(this, 800)
            }
        }, 800)
    }




    private fun checkCollision() {
        if (matrix[7][harryLane].tag == "voldemort") {
            lives -= 1
            updateLives()
            Toast.makeText(this, "Voldemort hit Harry!", Toast.LENGTH_SHORT).show()
            
            if (lives == 0) endGame()

            matrix[7][harryLane].apply {
                visibility = View.INVISIBLE
                setImageResource(0) // Remove Voldemort image
                tag = null // Clear the tag
            }
        }
    }


    private fun updateLives() {
        for (i in hearts.indices) {
            hearts[i].visibility = if (i<lives) View.VISIBLE else View.INVISIBLE
        }
    }
    

    private fun endGame() {
        isGameRunning = false
        Toast.makeText(this, "Game over, Voldemort killed you",Toast.LENGTH_LONG).show()
    }

}





