package com.example.hybridsurvey.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.hybridsurvey.R
import com.example.hybridsurvey.Survey
import com.example.hybridsurvey.localDatabase.AppDatabase
import com.example.hybridsurvey.localDatabase.DatabaseSingleton
import com.google.gson.Gson
import kotlinx.coroutines.*

class DatabaseUploadService : Service() {
    //How can I stop the foreground service when my app is no longer using it?
    private companion object {
        const val TAG = "tag"
        const val NOTIFICATION_CHANNEL_ID = "DatabaseUploadServiceChannel"
        const val NOTIFICATION_ID = 1
        const val UPLOAD_INTERVAL_MS = 5000L
    }

    private var isRunning = false
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            checkInternetAndUploadDatabase()
            if (isRunning) {
                handler.postDelayed(this, UPLOAD_INTERVAL_MS)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (!isRunning) {
            isRunning = true
            handler.post(runnable)
            startForeground(NOTIFICATION_ID, getNotification())
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun getNotification(): Notification {
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Database Upload Service")
            .setContentText("Running...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Database Upload Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        return builder.build()
    }

    private suspend fun getAllSurveyData(db: AppDatabase): List<Survey> {
        return withContext(Dispatchers.IO) {
            db.surveyDao().getAllSurveyData()
        }
    }

    private fun getDataAndConvertToJson(): String? {
        val db = DatabaseSingleton.getInstance(applicationContext)
        val deferred: Deferred<List<Survey>> = GlobalScope.async {
            getAllSurveyData(db)
        }
        var result: List<Survey> = emptyList()
        runBlocking {
            result = deferred.await()

        }
        return Gson().toJson(result)
    }

    private fun checkInternetAndUploadDatabase() {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            // Upload database file to server using a library like Retrofit or Volley
            /** get database and convert to json*/

            getDataAndConvertToJson()?.let { Log.i("TAG", it) }

            Log.d(TAG, "Database file uploaded to server.")
        } else {
            Log.d(TAG, "Internet not connected. Service Stopped")
        }
    }
}