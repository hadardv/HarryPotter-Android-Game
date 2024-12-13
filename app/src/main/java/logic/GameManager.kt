package logic

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.example.harrypottergame.R
import kotlinx.coroutines.Runnable
import kotlin.random.Random

class GameManager(private val gridLayout: GridLayout,
                  private val hearts: Array<AppCompatImageView>,
                  private val context: Context) {

    private var handler = Handler(Looper.getMainLooper())
    private var isGameRunning = true
    private var lives = 3
    private var harryLane = 1
    private val rows = 8
    private val cols = 3
    private val grid: Array<Array<AppCompatImageView>> = Array(rows) {
        Array(cols) { AppCompatImageView(context) }
    }

    init {
        initializeGrid()
    }

    private fun initializeGrid() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val imageView = createGridCell()
                grid[row][col] = imageView
                gridLayout.addView(imageView)
            }
        }

        // Set Harry's initial position
        grid[rows - 1][harryLane].apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.harry)
        }
    }

    private fun createGridCell(): AppCompatImageView {
        return AppCompatImageView(context).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            scaleType = ImageView.ScaleType.FIT_CENTER
            visibility = View.INVISIBLE
        }
    }

    fun startGame() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isGameRunning) return

                moveVoldemortDown()
                //checkCollision()
                spawnVoldemorts()

                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    fun moveHarry(index: Int) {
        grid[rows - 1][harryLane].apply {
            visibility = View.INVISIBLE
            setImageResource(0)
        }
        harryLane = (harryLane + index).coerceIn(0, cols - 1)
        grid[rows - 1][harryLane].apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.harry)
        }
    }

    fun moveVoldemortDown() {
        for (row in rows - 2 downTo 0) { // Start from the second last row and move up
            for (col in 0 until cols) {
                val currentCell = grid[row][col]
                if (currentCell.tag == "voldemort") {
                    val newCell = grid[row + 1][col]

                    if (newCell.tag == "voldemort") {
                        continue
                    }


                    // Handle Voldemorts moving to row 7
                    if (row + 1 == rows - 1) {
                        // Clear Voldemort if it's at row 7
                        currentCell.apply {
                            visibility = View.INVISIBLE
                            setImageResource(0)
                            tag = null
                        }

                        newCell.apply {
                            tag = "voldemort"
                        }
                        checkCollision()
                        newCell.apply {
                            tag = null
                        }
                        continue

                    }

                    // Move Voldemort down
                    newCell.apply {
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.lord_voldemort)
                        tag = "voldemort"
                    }

                    // Clear old position
                    currentCell.apply {
                        visibility = View.INVISIBLE
                        setImageResource(0)
                        tag = null
                    }

                }
            }
        }
    }

    fun spawnVoldemorts() {
        val voldemortCount = Random.nextInt(1, 2) // Random number of Voldemorts
        val positions = (0 until cols).shuffled().take(voldemortCount)

        for (col in positions) {
            val topCell = grid[0][col]

            // Skip if there's already a Voldemort in this cell
            if (topCell.tag == "voldemort") continue

            topCell.apply {
                visibility = View.VISIBLE
                setImageResource(R.drawable.lord_voldemort)
                tag = "voldemort"
            }
        }
    }


    fun checkCollision() {
        val harryCell = grid[rows - 1][harryLane]

        // Check if the Voldemort's tag is in Harry's lane
        if (harryCell.tag == "voldemort") {
            lives-- // Reduce lives
            updateLives()

            Toast.makeText(context, "Voldemort hit Harry!", Toast.LENGTH_SHORT).show()

            harryCell.apply {
                tag = null
            }

            harryCell.apply {
                visibility = View.VISIBLE
                setImageResource(R.drawable.harry)
            }

            // End game if no lives are left
            if (lives <= 0) {
                endGame()
            }
        }
    }

    private fun updateLives() {
        for (i in hearts.indices) {
            hearts[i].visibility = if (i < lives) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun endGame() {
        isGameRunning = false
        handler.removeCallbacksAndMessages(null)
        Toast.makeText(context, "Voldemort killed Harry :(", Toast.LENGTH_SHORT).show()
    }
}
