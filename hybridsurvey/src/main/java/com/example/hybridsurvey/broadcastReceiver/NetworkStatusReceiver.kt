package com.example.hybridsurvey.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.example.hybridsurvey.services.DatabaseUploadService

class NetworkStatusReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {

                Log.d("Tag", "Internet found.")
                //start DatabaseUploadService

                val serviceIntent = Intent(context, DatabaseUploadService::class.java)

                context.startForegroundService(serviceIntent)


            }else{
                //stop DatabaseUploadService
                Log.d("Tag", "Internet lost.")
                val serviceIntent = Intent(context, DatabaseUploadService::class.java)
                context.stopService(serviceIntent)
            }
        }
    }
}