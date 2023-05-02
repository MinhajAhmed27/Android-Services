package com.example.androidservices.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.androidservices.MainActivity
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

        val intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)


        val builder = Notification.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setContentTitle("Foreground App Running..")
            .setContentText("noting")

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