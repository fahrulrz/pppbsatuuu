package com.example.nyobanyoba.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ppbo-api.vercel.app/QQ1Xw/") // Ganti dengan URL Firebase Realtime Database
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FirebaseApi by lazy {
        retrofit.create(FirebaseApi::class.java)
    }
}