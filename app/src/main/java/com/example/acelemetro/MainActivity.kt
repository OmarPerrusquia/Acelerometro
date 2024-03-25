package com.example.acelemetro

import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var sensorEventListener: SensorEventListener? = null
    private var whip = 0
    lateinit var imageG:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageG = findViewById(R.id.imageViewG)
        imageG.setImageResource(R.drawable.grillo)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (sensor == null) {
            finish()
        }

        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val x = event?.values?.get(0)
                println("valor giro $x")
                x?.let {
                    if (it < -5 && whip == 0) {
                        whip++
                        window.decorView.setBackgroundColor(Color.CYAN)
                        sound()
                    } else if (it > 5 && whip == 1) {
                        whip++
                        window.decorView.setBackgroundColor(Color.RED)
                        sound()
                    }
                    if (whip == 2) {
                        sound()
                        whip = 0
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }
        start()
    }

    private fun sound() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.mi_grillo)
        mediaPlayer.start()
    }

    private fun start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun stop() {
        sensorManager.unregisterListener(sensorEventListener)
    }

    override fun onPause() {
        stop()
        super.onPause()
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        start()
        super.onTopResumedActivityChanged(isTopResumedActivity)
    }
}
