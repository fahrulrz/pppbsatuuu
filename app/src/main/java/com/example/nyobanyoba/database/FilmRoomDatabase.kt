package com.example.nyobanyoba.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nyobanyoba.model.FilmResponse

@Database(entities = [FilmResponse::class], version = 1, exportSchema = false)
abstract class FilmRoomDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao?

    companion object {
        @Volatile
        private var INSTANCE: FilmRoomDatabase? = null
        fun getDatabase(context: Context): FilmRoomDatabase? {
            if (INSTANCE == null){
                synchronized(FilmRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, FilmRoomDatabase::class.java, "film_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}