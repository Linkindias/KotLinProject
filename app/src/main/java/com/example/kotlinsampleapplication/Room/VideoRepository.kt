package com.example.kotlinsampleapplication.Room

import androidx.annotation.WorkerThread

class VideoRepository(val videoDao:VideoDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertItem(video: VideoEntity) {
        videoDao.insert(video)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(videos: List<VideoEntity>) {
        videoDao.insertAll(videos)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        videoDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(video: VideoEntity) {
        videoDao.delete(video)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(video: VideoEntity) {
        videoDao.update(video)
    }

    fun getAll(): List<VideoEntity>{
        return videoDao.getAll()
    }

    fun getVideoByVariable(path: String, fileName: String, type: String): VideoEntity{
        return videoDao.getVideoByVariable(path, fileName, type)
    }
}