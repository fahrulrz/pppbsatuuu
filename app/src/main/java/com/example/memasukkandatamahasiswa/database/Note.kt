package com.example.memasukkandatamahasiswa.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "note_table")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "nim")
    val nim: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "address")
    val address: String
)
