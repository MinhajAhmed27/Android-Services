package com.example.hybridsurvey

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hybridsurvey.base.BaseActivity
import com.example.hybridsurvey.broadcastReceiver.NetworkStatusReceiver


open class MainActivity : BaseActivity() {
    private val networkStatusReceiver = NetworkStatusReceiver()
    private val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    private val MY_STORAGE_PERMISSION = 100


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()


        registerReceiver(networkStatusReceiver, filter)


        val goToSurvey = findViewById<View>(R.id.goToSurvey)
        goToSurvey.setOnClickListener {
            startActivity(Intent(this, SurveyActivity::class.java))
            Log.d("Tag", Environment.getExternalStorageDirectory().absolutePath  + "/documents/app" )

        }
    }


    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it at runtime
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_STORAGE_PERMISSION
            )
        } else {
            // Permission is already granted, continue with your app logic
            // ...
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_STORAGE_PERMISSION -> {
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