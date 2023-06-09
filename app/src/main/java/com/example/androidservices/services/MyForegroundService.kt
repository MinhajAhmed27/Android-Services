package com.example.androidservices.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.androidservices.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyForegroundService : Service() {

    private val CHANNEL_ID = "androidservices.apps.notifications"
    private val NOTIFICATION_ID = 1101


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()

        GlobalScope.launch(Dispatchers.Default){
            try {
                while (true){
                    Log.e("Service", "Foreground service is running...")
                    delay(1000)
                }
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
        }

        showNotificationAndStartService()

        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotificationAndStartService() {
        val builder = Notification.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Foreground App Running..")
            .setContentText("noting")

        /** This is the main function (startForeground) to start the service in foreground,
         * if you run the service without it by calling the startForegroundService(Intent) from Main then
         * the service only run in Background, means it would be killed once app is terminated**/
        //here you must show the notification
        startForeground(NOTIFICATION_ID,builder.build())

    }

    private fun createNotification(){

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID,"Foreground App",
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Foreground service is running..."
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}