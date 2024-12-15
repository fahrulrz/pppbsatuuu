package com.example.nyobanyoba.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "film_table")
data class FilmResponse(
    @SerializedName("_id")
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,
    @SerializedName("image")
    @ColumnInfo(name = "image")
    val image: String,
    @SerializedName("duration")
    @ColumnInfo(name = "duration")
    val duration: Int,
    @SerializedName("synopsis")
    @ColumnInfo(name = "synopsis")
    val synopsis: String,
    @SerializedName("isFavorite")
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean = false,
) : Parcelable
