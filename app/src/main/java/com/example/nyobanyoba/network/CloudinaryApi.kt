package com.example.nyobanyoba.network

import android.util.Log
import com.cloudinary.Cloudinary
import java.io.File
import com.cloudinary.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

// Objek untuk mengatur konfigurasi Retrofit
object CloudinaryApi {
    private const val CLOUDINARY_URL = "cloudinary://578989197745588:cJ16GU4pD89nhDfrYRUI5eFYL8U@dddvmamju"

    private val parsedConfig: Map<String, String> by lazy {
        // Ekstrak konfigurasi dari URL Cloudinary
        val regex = Regex("cloudinary://(.+):(.+)@(.+)")
        // Validasi format URL
        val matchResult = regex.find(CLOUDINARY_URL)
        // Mengembalikan konfigurasi jika valid
        if (matchResult != null) {
            mapOf(
                "api_key" to matchResult.groupValues[1],
                "api_secret" to matchResult.groupValues[2],
                "cloud_name" to matchResult.groupValues[3]
            )
        } else {
            // Jika format URL tidak valid, lemparkan pengecualian
            throw IllegalArgumentException("Invalid CLOUDINARY_URL format")
        }
    }

    // Konfigurasi Retrofit untuk mengakses API Cloudinary
    private val baseUrl by lazy {
        // Membangun URL dasar dari konfigurasi
        "https://api.cloudinary.com/v1_1/${parsedConfig["cloud_name"]}/"
    }

    // Membuat instance Cloudinary
    val cloudinaryService: CloudinaryService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryService::class.java)
    }
}