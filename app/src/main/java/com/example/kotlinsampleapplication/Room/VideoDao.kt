package com.example.kotlinsampleapplication.Room

import androidx.room.*

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<VideoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE) //新增物件時和舊物件發生衝突後的處置 REPLACE 蓋掉、ROLLBACK 閃退、ABORT 閃退 (默認)、FAIL 閃退、IGNORE 忽略，還是舊的資料
    fun insert(video: VideoEntity)

    @Query("SELECT * FROM videoInfo WHERE path Like :path and fileName Like :fileName and type Like :type")
    fun findByVariable(path: String, fileName: String, type: String): VideoEntity

    @Query("SELECT * FROM videoInfo")
    fun getAll(): List<VideoEntity>

    @Query("DELETE FROM videoInfo")
    fun deleteAll()

    @Delete
    fun delete(video: VideoEntity)

    @Update
    fun update(video: VideoEntity)
}