package com.example.workmanager.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanager.R
import com.example.workmanager.network.ImageFileApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

class DownloadWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        setService()
        val response = ImageFileApi.instance.downloadImage()
        delay(5000L)

        response.body()?.let { body ->
            return withContext(Dispatchers.IO) {

                val file = File(context.cacheDir, "image.png")
                val outputStream = FileOutputStream(file).use { stream ->
                    try {
                        stream.write(body.bytes())
                    } catch (e: IOException) {
                        return@withContext Result.failure(workDataOf(WorkerKeys.Error_MSG to e.toString()))
                    }
                }
                Result.success(
                    workDataOf(
                        WorkerKeys.Image_URI to file.toURI().toString()
                    )
                )
            }
        }
        if(!response.isSuccessful){
            if(response.code().toString().startsWith("5")){
                Result.retry()
            }else{
                Result.failure(workDataOf(
                    WorkerKeys.Error_MSG to "Network Error"
                ))
            }
        }
        return Result.failure(workDataOf(
            WorkerKeys.Error_MSG to "Unknown Error"
        ))
    }

    private suspend fun setService() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, "Channel_ID")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentText("file downloading...")
                    .setContentTitle("file downloader")
                    .build()
            )
        )

    }
}