package com.example.androidservices.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyBackgroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        GlobalScope.launch(Dispatchers.Default){
            try {
                while (true){
                    Log.e("Service", "Background service is running...")
                    delay(1000)
                }
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }

        Log.e("Service", "outside...")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
    }


}