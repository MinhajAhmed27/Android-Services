package com.example.androidservices

import android.app.ActivityManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androidservices.services.MyBackgroundService
import com.example.androidservices.services.MyForegroundService


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val next = findViewById<View>(R.id.btn_next)
        next.setOnClickListener {
            startActivity(Intent(this, TimerServiceActivity::class.java))
        }

        val intentBackground: Intent = Intent(this, MyBackgroundService::class.java)
//        startService(intentBackground)

        val intentForeground: Intent = Intent(this, MyForegroundService::class.java)

        if(!isForegroundServiceRunning()){
//            startForegroundService(intentForeground)

        }

    }

    private fun isForegroundServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (MyForegroundService::class.java.name == service.service.className) {
                return true
            }
        }
        return false

    }
}