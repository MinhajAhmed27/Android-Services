package com.example.androidservices.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidservices.Survey
import com.example.androidservices.SurveyDataDao

@Database(entities = [Survey::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDataDao
}