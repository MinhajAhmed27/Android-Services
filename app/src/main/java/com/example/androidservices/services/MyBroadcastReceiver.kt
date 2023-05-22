package com.example.androidservices.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, receiveIntent: Intent?) {
        if (receiveIntent != null) {
            if(receiveIntent.action.equals(Intent.ACTION_BOOT_COMPLETED)){
                val intent = Intent(context,MyForegroundService::class.java)
                context?.startForegroundService(intent)
            }
        }
    }
}