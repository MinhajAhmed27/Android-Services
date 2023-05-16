package com.example.workmanager.worker

import android.content.Context
import android.graphics.*
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ColorFilterWorker(
    val context: Context,
    val workParams: WorkerParameters
):CoroutineWorker(context,workParams) {
    override suspend fun doWork(): Result {
        /** inputData **/ //give the key/value pairs we pass from downloadWorker or previous worker
        val imageFile = workParams.inputData.getString(WorkerKeys.Image_URI)?.toUri()?.toFile()

        return imageFile?.let { file ->
            withContext(Dispatchers.IO) {
                val bmp = BitmapFactory.decodeFile(file.absolutePath)
                val resultBmp = bmp.copy(bmp.config, true)
                val paint = Paint()
                paint.colorFilter = LightingColorFilter(0x08FF04, 1)
                val canvas = Canvas(resultBmp)
                canvas.drawBitmap(resultBmp, 0f, 0f, paint)

                val resultImageFile = File(context.cacheDir, "new-image.png")
                val outputStream = FileOutputStream(resultImageFile)
                val successful = resultBmp.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    outputStream
                )
                if(successful) {
                    Result.success(
                        workDataOf(
                            WorkerKeys.FilterImage_URI to resultImageFile.toUri().toString()
                        )
                    )
                } else Result.failure()
            }
        } ?: Result.failure()
    }

}