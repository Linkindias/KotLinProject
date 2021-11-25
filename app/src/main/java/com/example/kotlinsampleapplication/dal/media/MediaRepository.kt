package com.example.kotlinsampleapplication.dal.media

import androidx.annotation.WorkerThread

class MediaRepository(val mediaDao:MediaDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insertItem(media: MediaEntity) {
        mediaDao.insert(media)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insertAll(medias: List<MediaEntity>) {
        mediaDao.insertAll(medias)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun deleteAll() {
        mediaDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun delete(media: MediaEntity) {
        mediaDao.delete(media)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
     fun update(media: MediaEntity) {
        mediaDao.update(media)
    }

    fun getAll(): List<MediaEntity>{
        return mediaDao.getAll()
    }

    fun getMediaByType(type: String): List<MediaEntity>{
        return mediaDao.getMediaByVariable("%", "%", type)
    }

    fun getMediaByPath(path: String): MediaEntity? {
        var result = mediaDao.getMediaByVariable(path, "%", "%")
        if (result.isNotEmpty()) return result[0]
        return null
    }

    fun getMediaByFileName(fileName: String): MediaEntity? {
        var result = mediaDao.getMediaByVariable("%", fileName, "%")
        if (result.isNotEmpty()) return result[0]
        return null
    }
}