package com.example.hybridsurvey.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hybridsurvey.Survey
import com.example.hybridsurvey.SurveyDataDao

@Database(entities = [Survey::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDataDao
}