package com.example.androidservices

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidservices.services.TimerService

class TimerServiceActivity : AppCompatActivity() {
    lateinit var textTimer : TextView
    lateinit var btnStart : Button
    lateinit var btnPause : Button
    private var handler = Handler(Looper.getMainLooper()){
        textTimer.text = it.what.toString()
        true
    }


    private lateinit var timerBinder : TimerService.TimerBinder
    var isConnected = false

    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder.setHandler(handler)
            isConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnected = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_service)

        textTimer = findViewById(R.id.txt_counter)
        btnStart = findViewById(R.id.btn_start)
        btnPause = findViewById(R.id.btn_pause)


        bindService(Intent(this,TimerService::class.java),serviceConnection,BIND_AUTO_CREATE)

        btnStart.setOnClickListener {
            if (isConnected) timerBinder.startTimer()

        }
        btnPause.setOnClickListener {
            if (isConnected) timerBinder.pauseTime()
            Toast.makeText(this,"Paused",Toast.LENGTH_SHORT).show()
            textTimer.text = "00"
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }


}