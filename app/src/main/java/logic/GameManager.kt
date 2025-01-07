package logic

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.example.harrypottergame.R
import kotlinx.coroutines.Runnable
import kotlin.random.Random
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager


class GameManager(private val gridLayout: GridLayout,
                  private val hearts: Array<AppCompatImageView>,
                  private val context: Context) {

    private var handler = Handler(Looper.getMainLooper())
    private var isGameRunning = true
    private var lives = 3
    var harryLane = 2
    private val rows = 10
    private val cols = 5
    private var gameSpeed: Long = 800
    private val grid: Array<Array<AppCompatImageView>> = Array(rows) {
        Array(cols) { AppCompatImageView(context) }
    }

    init {
        initializeGrid()
    }

    // Initializing the game grid with harry in the middle lane and putting imageView in each cell of the matrix
    private fun initializeGrid() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val imageView = createGridCell()
                grid[row][col] = imageView
                gridLayout.addView(imageView)
            }
        }

        grid[rows - 1][harryLane].apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.harry)
        }
    }

    // function to actually create each cell in the matrix to have AppCompatImageView
    //to be ready to hold voldemort's picture
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

    fun setSpeed(speed: Long) {
        gameSpeed = speed
    }

    fun startGame() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!isGameRunning) return

                moveVoldemortDown()
                newVoldemorts()

                handler.postDelayed(this,gameSpeed)
            }
        }, gameSpeed)
    }

    // a function to move harry left or right
    fun moveHarry(newLane: Int) {
        // Ensure the new lane is within bounds
        if (newLane < 0 || newLane >= cols) return

        // Hide Harry in the current lane
        grid[rows - 1][harryLane].apply {
            visibility = View.INVISIBLE
            setImageResource(0)
        }

        // Update Harry’s lane position
        harryLane = newLane

        // Show Harry in the new lane
        grid[rows - 1][harryLane].apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.harry)
        }
    }


    // a function to move an existing voldemort image one cell down until row 7
    fun moveVoldemortDown() {
        for (row in rows - 2 downTo 0) { // Start from the second last row and move up
            for (col in 0 until cols) {
                val currentCell = grid[row][col]
                if (currentCell.tag == "voldemort") {
                    val newCell = grid[row + 1][col]

                    if (newCell.tag == "voldemort") {
                        continue
                    }


                    // if voldemort reaches row 7
                    if (row + 1 == rows - 1) {
                        // Clear voldemort if it's at row 7
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

    // bring down voldemorts randomly
    fun newVoldemorts() {
        val voldemortCount = Random.nextInt(0, 2)
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

//checking if voldemort and harry collied
    fun checkCollision() {
        val harryCell = grid[rows - 1][harryLane]
        // Check if the Voldemort's tag is in Harry's lane
        if (harryCell.tag == "voldemort") {
            lives--
            updateLives()
            triggerVibration()
            Toast.makeText(context, "Voldemort hit Harry!", Toast.LENGTH_SHORT).show()

            harryCell.apply {
                tag = null
            }

            harryCell.apply {
                visibility = View.VISIBLE
                setImageResource(R.drawable.harry)
            }

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

        handler.postDelayed({
            resetGame()
        }, 2000)
    }

    private fun triggerVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31 and above
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else { // Below API 31
            @Suppress("DEPRECATION") // Suppress deprecation warning for older APIs
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }
    //to start the game all over again after voldemort killed harry we reset the game's settings

    private fun resetGame() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                grid[row][col].apply {
                    visibility = View.INVISIBLE
                    setImageResource(0)
                    tag = null
                }
            }
        }
        harryLane = 1
        grid[rows - 1][harryLane].apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.harry)
        }

        lives = 3
        updateLives()
        isGameRunning = true
        startGame()
    }
}
