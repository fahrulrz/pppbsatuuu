package com.example.nyobanyoba.repository

import com.example.nyobanyoba.network.CloudinaryApi
import okhttp3.MultipartBody
import retrofit2.Response
import okhttp3.ResponseBody

class CloudinaryRepository {
    private val service = CloudinaryApi.cloudinaryService

    suspend fun uploadImage(file: MultipartBody.Part, uploadPreset: String): Response<ResponseBody> {
        return service.uploadImage(file, uploadPreset)
    }
}
