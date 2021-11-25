package com.example.kotlinsampleapplication.dal.media

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.migration.Migration
import com.example.base.Common


@Database(entities = arrayOf(MediaEntity::class), version = 2, exportSchema = false)
@TypeConverters(Common::class)
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

        fun getDatabase(context: Context?): MediaDBHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context!!, MediaDBHelper::class.java,"mediaInfo" )
                                    .addMigrations(migration2)
                                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}