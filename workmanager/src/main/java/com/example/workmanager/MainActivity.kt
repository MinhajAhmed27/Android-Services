package com.example.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.Image
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.work.*
import coil.compose.rememberImagePainter
import androidx.compose.foundation.Image
import com.example.workmanager.ui.theme.AndroidservicesTheme
import com.example.workmanager.worker.ColorFilterWorker
import com.example.workmanager.worker.DownloadWorker
import com.example.workmanager.worker.WorkerKeys

class MainActivity : ComponentActivity() {
    private val CHANNEL_ID = "Channel_ID"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //creating channel for foreground notification

        val channel =
            NotificationChannel(CHANNEL_ID, "IMAGE DOWNLOADER", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)

        val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(
                    NetworkType.CONNECTED
                ).build()
            ).build()

        val colorFilterRequest = OneTimeWorkRequestBuilder<ColorFilterWorker>()
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        setContent {
            val workInfo = workManager.getWorkInfosForUniqueWorkLiveData("download")
                .observeAsState() //observe updates like live data
                .value
            val downloadInfo = remember(key1 = workInfo) {
                workInfo?.find { it.id == downloadRequest.id }
            }
            val colorFilterInfo = remember(key1 = workInfo) {
                workInfo?.find { it.id == colorFilterRequest.id }
            }
            val imageUri by derivedStateOf {
                val downloadUri = downloadInfo?.outputData?.getString(WorkerKeys.Image_URI)
                    ?.toUri()
                val filterUri = colorFilterInfo?.outputData?.getString(WorkerKeys.FilterImage_URI)
                    ?.toUri()
                filterUri ?: downloadUri
            }
            AndroidservicesTheme {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberImagePainter(
                                data = uri
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Button(
                        onClick = {
                            workManager
                                .beginUniqueWork(
                                    "download",
                                    ExistingWorkPolicy.KEEP,
                                    downloadRequest
                                )
                                .then(colorFilterRequest)
                                .enqueue()
                        }, enabled = downloadInfo?.state != WorkInfo.State.RUNNING
                    ) {
                        Text(text = "Start Download")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when (downloadInfo?.state) {
                        WorkInfo.State.RUNNING -> Text("Downloading...")
                        WorkInfo.State.SUCCEEDED -> Text("Download succeeded")
                        WorkInfo.State.FAILED -> Text("Download failed")
                        WorkInfo.State.CANCELLED -> Text("Download cancelled")
                        WorkInfo.State.ENQUEUED -> Text("Download enqueued")
                        WorkInfo.State.BLOCKED -> Text("Download blocked")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when (colorFilterInfo?.state) {
                        WorkInfo.State.RUNNING -> Text("Applying filter...")
                        WorkInfo.State.SUCCEEDED -> Text("Filter succeeded")
                        WorkInfo.State.FAILED -> Text("Filter failed")
                        WorkInfo.State.CANCELLED -> Text("Filter cancelled")
                        WorkInfo.State.ENQUEUED -> Text("Filter enqueued")
                        WorkInfo.State.BLOCKED -> Text("Filter blocked")
                    }

                }
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        AndroidservicesTheme {
            Greeting("Android")
        }
    }
}