package com.example.proenglishscoretracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [EnglishInfo::class], version = 1)
abstract class EnglishInfoDatabase : RoomDatabase() {
    abstract fun englishInfoDao(): EnglishInfoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: EnglishInfoDatabase ? = null

        fun getEnglishInfoDatabase(context: Context): EnglishInfoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    EnglishInfoDatabase::class.java, "english_info_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
