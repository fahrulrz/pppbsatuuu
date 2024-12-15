package com.example.nyobanyoba.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nyobanyoba.model.FilmResponse

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFilm(film: FilmResponse)

    @Update
    fun updateFilm(film: FilmResponse)

    @Delete
    fun deleteFilm(film: FilmResponse)

    @Query("SELECT * FROM film_table")
    fun getAllFilms(): List<FilmResponse>

    @Query("SELECT EXISTS(SELECT 1 FROM film_table WHERE id = :filmId)")
    fun isFilmExists(filmId: String): Boolean
}