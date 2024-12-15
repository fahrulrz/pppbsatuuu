package com.example.nyobanyoba.network

import com.example.nyobanyoba.model.Film
import com.example.nyobanyoba.model.FilmResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FirebaseApi {
    // Route untuk menambahkan data
    @POST("films")
    suspend fun createFilm(@Body film: Film): Response<Film>

    // Route untuk mengambil data
    @GET("films")
    suspend fun getFilms(): Response<List<FilmResponse>>

    // Route untuk update data
    @POST("films/{id}")
    suspend fun updateFilm(
        @Path("id") id: String,
        @Body film: FilmResponse
    ): Response<FilmResponse>

    // Route untuk delete data
    @DELETE("films/{id}")
    suspend fun deleteFilm(
        @Path("id") id: String
    ): Response<Void>

}