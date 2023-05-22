package com.example.hybridsurvey

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SurveyDataDao {

    @Insert
    suspend fun insertSurveyData(survey: Survey)

    @Query("SELECT * FROM survey")
    suspend fun getAllSurveyData(): List<Survey>
}

