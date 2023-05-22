package com.example.androidservices.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.androidservices.R
import com.example.androidservices.Survey
import com.example.androidservices.localDatabase.AppDatabase
import com.example.androidservices.localDatabase.DatabaseSingleton
import com.example.workmanager.network.SurveyUploadApi
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.File
import java.lang.Runnable

class ConnectivityService : Service() {

    //How can I stop the foreground service when my app is no longer using it?
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // TODO: Initialize the service
        checkInternetConnectivity()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the service as a foreground service
        val notification = createNotification()
        startForeground(1, notification)

        // TODO: Start the service logic
        Log.i("TAG", "Work onStartCommand")
        checkInternetConnectivity()
        return START_STICKY
    }

    private fun createNotification(): Notification {
        // Create a notification builder
        val builder = NotificationCompat.Builder(this, "default")
            .setContentTitle("Connectivity Service")
            .setContentText("The service is running.")
            .setSmallIcon(R.drawable.ic_launcher_background)

        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Build and return the notification
        return builder.build()
    }

    private fun checkInternetConnectivity() {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (isConnected) {

            /** get database and convert to json*/

            getDataAndConvertToJson()?.let { Log.i("TAG", it) }

            // TODO: Upload saved data to server
//            val response = SurveyUploadApi.instance.uploadSurvey()

        } else {
            Log.i("TAG", "No internet connectivity")
        }
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
        var result:List<Survey> = emptyList()
        runBlocking {
            result = deferred.await()

        }
        return Gson().toJson(result)
    }
}

/*
class ConnectivityService : Service() {

    private val TAG = "ConnectivityService"

    private val mHandler = Handler()
    private val mRunnable = object : Runnable {
        override fun run() {
            checkInternetConnectivity()
            mHandler.postDelayed(this, 60 * 1000) // check every 60 seconds
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "ConnectivityService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "ConnectivityService started")
        mHandler.post(mRunnable)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "ConnectivityService destroyed")
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        // not used
        return null
    }

    private fun checkInternetConnectivity() {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (isConnected) {

            */
/** get database and convert to json*//*


            getDataAndConvertToJson()?.let { Log.i(TAG, it) }

            // TODO: Upload saved data to server
//            val response = SurveyUploadApi.instance.uploadSurvey()

        } else {
            Log.i(TAG, "No internet connectivity")
        }
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
        var result:List<Survey> = emptyList()
        runBlocking {
            result = deferred.await()

        }
        return Gson().toJson(result)
    }
}*/
