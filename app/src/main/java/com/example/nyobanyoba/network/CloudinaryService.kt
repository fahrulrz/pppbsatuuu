package com.example.nyobanyoba.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

// Interface Retrofit untuk mendefinisikan endpoint API Cloudinary
interface CloudinaryService {
    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Query("upload_preset") uploadPreset: String
    ): Response<ResponseBody>

}