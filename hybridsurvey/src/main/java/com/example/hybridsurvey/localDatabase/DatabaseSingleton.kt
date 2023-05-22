package com.example.hybridsurvey.localDatabase

import android.content.Context
import android.os.Environment
import androidx.room.Room
import java.io.File

object DatabaseSingleton {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                Environment.getExternalStorageDirectory().absolutePath +"/Documents/SurveyDatabase" + File.separator + "my-database"
            ).build()
            instance = database
            database
        }
    }
}