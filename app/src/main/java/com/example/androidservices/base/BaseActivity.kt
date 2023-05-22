package com.example.androidservices.base

import android.app.ActivityManager
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity(){

    fun isServiceRunning(className:String): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (className == service.service.className) {
                return true
            }
        }
        return false
    }
}
