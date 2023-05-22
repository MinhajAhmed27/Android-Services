package com.example.androidservices

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.androidservices.localDatabase.AppDatabase
import com.example.androidservices.localDatabase.DatabaseSingleton
import com.example.workmanager.network.SurveyUploadApi
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_survey.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

class SurveyActivity : AppCompatActivity() {
    lateinit var result : List<Survey>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        val gson = Gson()
        result = emptyList()

//        this.getExternalFilesDir("")
/** Change Direcorty to download or document **/

    val db = DatabaseSingleton.getInstance(applicationContext)

        btn_submit.setOnClickListener {

            var survey = Survey(
                Random.nextInt(),
                editTextName.text.toString(),
                editTextEmailName.text.toString(),
                "","")

            // Insert the Survey object into the database
            GlobalScope.launch {
                db.surveyDao().insertSurveyData(survey)
                editTextName.setText("")
                editTextEmailName.setText("")
            }
        }

        btn_show_all.setOnClickListener {

            GlobalScope.launch {
                result = db.surveyDao().getAllSurveyData()
                /** Add error handling if data is not submitted **/
                Log.d("List-Tag", Environment.getExternalStorageDirectory().absolutePath +"/Documents" + File.separator + "my-database")

            }

            Toast.makeText(this,gson.toJson(result),Toast.LENGTH_SHORT).show()
            Log.d("List-Tag", gson.toJson(result))

        }

    }
}