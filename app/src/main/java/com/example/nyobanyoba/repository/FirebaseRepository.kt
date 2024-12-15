package com.example.nyobanyoba.repository

import com.example.nyobanyoba.model.Film
import com.example.nyobanyoba.model.FilmResponse
import com.example.nyobanyoba.network.FirebaseApi
import retrofit2.Response

class FirebaseRepository(private val firebaseApi: FirebaseApi) {
    suspend fun updateFilm(id: String, filmResponse: FilmResponse): Response<FilmResponse> {
        return firebaseApi.updateFilm(id, filmResponse)
    }
}