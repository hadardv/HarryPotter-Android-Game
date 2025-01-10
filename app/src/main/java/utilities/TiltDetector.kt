package utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import interfaces.TiltCallback

class TiltDetector (context: Context, private val tiltCallback: TiltCallback?) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor

    private var currentLane = 2

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return

            val xValue = event.values[0]
            val newLane = if (xValue > 4) {
                0 // Far Left
            } else if (xValue > 2) {
                1 // Left-Mid
            } else if (xValue in -2.0..2.0) {
                2 // Center
            } else if (xValue < -4) {
                4 // Far Right
            } else {
                3 // Right-Mid
            }


            if (newLane != currentLane) {
                currentLane = newLane
                when (currentLane) {
                    0 -> tiltCallback?.tiltLeft()
                    1 -> tiltCallback?.tiltLeft()
                    2 -> tiltCallback?.tiltCenter()
                    3 -> tiltCallback?.tiltRight()
                    4 -> tiltCallback?.tiltRight()
                }
            }
        }


        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // pass
        }
    }

    fun start(){
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }



    fun stop(){
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }
}