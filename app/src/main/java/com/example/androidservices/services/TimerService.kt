package com.example.androidservices.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {

    var isPaused = false
    private val binder = TimerBinder()
    lateinit var timerHandler: Handler


    inner class TimerBinder : Binder(){
        // Return this instance of LocalService so clients can call public methods.
//        fun getService(): TimerService = this@TimerService

        fun startTimer(){
            isPaused = true
            runTimer()
        }
        fun pauseTime(){
            isPaused = false
        }
        fun setHandler(handler: Handler){
            timerHandler = handler
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    /** Method for clients.  */
    fun runTimer() {
        Log.d("Countdown", "time.toString()")
        GlobalScope.launch(Dispatchers.Default){
            try {
                var counter = 100
                while(isPaused){
                    counter--
                    Log.d("Countdown", counter.toString())

                    if (::timerHandler.isInitialized){
                        timerHandler.sendEmptyMessage(counter)
                    }
                    delay(1000)
                }
            }catch (e:InterruptedException){
                Log.d("Countdown", e.printStackTrace().toString())

                e.printStackTrace()
            }
        }
    }
}