package com.example.kotlinsampleapplication.Room

import androidx.annotation.WorkerThread

class VideoRepository(val videoDao:VideoDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insertItem(video: VideoEntity) {
        videoDao.insert(video)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insertAll(videos: List<VideoEntity>) {
        videoDao.insertAll(videos)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun deleteAll() {
        videoDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun delete(video: VideoEntity) {
        videoDao.delete(video)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
     fun update(video: VideoEntity) {
        videoDao.update(video)
    }

    fun getAll(): List<VideoEntity>{
        return videoDao.getAll()
    }

    fun getVideoByVariable(path: String, fileName: String, type: String): VideoEntity{
        return videoDao.getVideoByVariable(path, fileName, type)
    }
}