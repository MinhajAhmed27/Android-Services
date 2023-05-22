package com.example.hybridsurvey.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*

interface SurveyUploadApi {

    @Multipart
    @POST("/wp-content/uploads/{extra_url}")
    fun uploadMerchantImage(
        @Path("extra_url", encoded = true) extra_url: String,
        @Part file: MultipartBody.Part
    ): Call<Void>

    @POST("/wp-content/uploads/2023/03/Android-Logo-2008-4.png")
    suspend fun uploadFile(
        @Path("extra_url", encoded = true) extra_url: String,
        @Part file: MultipartBody.Part,
        @HeaderMap headers: Map<String, String>
    ): Call<Void>

    @POST("/wp-content/uploads/2023/03/Android-Logo-2008-4.png")
    suspend fun uploadSurvey(surveyDataJson: String?)

    companion object{
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("https://tech-binary.com")
                .build()
                .create(SurveyUploadApi::class.java)
        }
    }

}