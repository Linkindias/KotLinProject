package com.example.kotlinsampleapplication.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kotlinsampleapplication.Base
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.annotation.NonNull

import androidx.room.migration.Migration




@Database(entities = arrayOf(MediaEntity::class), version = 2, exportSchema = false)
@TypeConverters(Base::class)
abstract class MediaDBHelper: RoomDatabase() {
    abstract fun mediaDao(): MediaDao

    companion object {
        private val migration2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                with(database) {
                }
            }
        }

        @Volatile
        private var INSTANCE: MediaDBHelper? = null

        fun getDatabase(context: Context): MediaDBHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, MediaDBHelper::class.java,"mediaInfo" )
                                    .addMigrations(migration2)
                                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}