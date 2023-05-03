package com.example.androidservices

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidservices.services.TimerService

class TimerServiceActivity : AppCompatActivity() {
    lateinit var textTimer : TextView
    lateinit var btnStart : Button
    lateinit var btnPause : Button
    lateinit var timerBinder : TimerService.TimerBinder
    var isConnected = false

    val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
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

        bindService(Intent(this,TimerService::class.java),serviceConnection, BIND_AUTO_CREATE)

        btnStart.setOnClickListener {
            if (isConnected) timerBinder.startTimer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

}