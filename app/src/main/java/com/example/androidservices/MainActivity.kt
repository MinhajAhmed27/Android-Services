package com.example.androidservices

import android.Manifest
import android.app.ActivityManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidservices.base.BaseActivity
import com.example.androidservices.broadcastReceiver.NetworkStatusReceiver
import com.example.androidservices.services.ConnectivityService
import com.example.androidservices.services.DatabaseUploadService
import com.example.androidservices.services.MyBackgroundService
import com.example.androidservices.services.MyForegroundService


open class MainActivity : BaseActivity() {
    private val networkStatusReceiver = NetworkStatusReceiver()
    private val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()



        val next = findViewById<View>(R.id.btn_next)
        next.setOnClickListener {
//            startActivity(Intent(this, TimerServiceActivity::class.java))
        }

        /** FOR SURVEY ONLY **/


        registerReceiver(networkStatusReceiver, filter)



    /** TODO: Ask Storage Permission**/

        val goToSurvey = findViewById<View>(R.id.goToSurvey)
        goToSurvey.setOnClickListener {
            startActivity(Intent(this, SurveyActivity::class.java))
            Log.d("Tag", Environment.getExternalStorageDirectory().absolutePath  + "/documents/app" )

        }

        /** FOR SURVEY ONLY **/


        val intentBackground: Intent = Intent(this, MyBackgroundService::class.java)
//        startService(intentBackground)

        val intentForeground: Intent = Intent(this, MyForegroundService::class.java)

//        =========================================================

        if(!isServiceRunning(MyForegroundService::class.java.name)){
//            startForegroundService(intentForeground)

        }
    }


    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it at runtime
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        } else {
            // Permission is already granted, continue with your app logic
            // ...
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted,// continue with your app logic
                    // ...
                } else {
                    // Permission is not granted, show a message to the user or disable the functionality that requires the permission
                    // ...
                }
                return
            }
            // Handle other permission requests if necessary
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkStatusReceiver, filter)
    }


}