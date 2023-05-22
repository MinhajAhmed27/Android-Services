package com.example.androidservices.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.androidservices.services.DatabaseUploadService
import com.example.androidservices.services.MyForegroundService

class NetworkStatusReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {

                Log.d("Tag", "Internet found.")
                //start DatabaseUploadService

                /*val intent = Intent(context, MyForegroundService::class.java)
                context?.startForegroundService(intent)*/
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