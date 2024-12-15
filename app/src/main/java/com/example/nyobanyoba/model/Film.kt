package com.example.nyobanyoba.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    @SerializedName("title")
    val title: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("synopsis")
    val synopsis: String,
    @SerializedName("isFavorite")
    val isFavorite: Boolean = false,
) : Parcelable
