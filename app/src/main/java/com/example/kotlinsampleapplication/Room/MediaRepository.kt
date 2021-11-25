package com.example.kotlinsampleapplication.Room

import androidx.annotation.WorkerThread

class MediaRepository(val videoDao:MediaDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insertItem(video: MediaEntity) {
        videoDao.insert(video)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insertAll(videos: List<MediaEntity>) {
        videoDao.insertAll(videos)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun deleteAll() {
        videoDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun delete(video: MediaEntity) {
        videoDao.delete(video)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
     fun update(video: MediaEntity) {
        videoDao.update(video)
    }

    fun getAll(): List<MediaEntity>{
        return videoDao.getAll()
    }

    fun getVideoByVariable(path: String, fileName: String, type: String): MediaEntity{
        return videoDao.getVideoByVariable(path, fileName, type)
    }
}