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
//    // Metode untuk upload gambar ke Cloudinary
//    @Multipart // Annotation untuk menunjukkan bahwa request berisi multipart data
//    @POST("image/upload") // Endpoint untuk upload gambar di Cloudinary
//    suspend fun uploadImage(
//        @Part file: MultipartBody.Part, // Bagian file yang akan di-upload
//        @Query("upload_preset") uploadPreset: String // Parameter query untuk upload preset
//    ): ResponseBody // Mengembalikan response dari server

    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Query("upload_preset") uploadPreset: String
    ): Response<ResponseBody>

}