package com.example.androidservices.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {

    var isPaused = false

    inner class TimerBinder : Binder(){
        fun startTimer(){
            isPaused = true
            runTimer()
        }
        fun pauseTime(){
            isPaused = true
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return TimerBinder()
    }

    //Ex: Timer Service
    private fun runTimer() {
        GlobalScope.launch(Dispatchers.Default){
            try {
                var time = 100
                while(isPaused){
                    time--
                    Log.d("Countdown", time.toString())
                    delay(1000)
                }
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }
    }

}