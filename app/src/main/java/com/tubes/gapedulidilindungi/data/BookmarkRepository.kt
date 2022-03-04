package com.tubes.gapedulidilindungi.data

import androidx.lifecycle.LiveData

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    val readAllData: LiveData<List<BookmarkData>> = bookmarkDao.readAllData()

    suspend fun addBookmark(bookmark: BookmarkData) {
        bookmarkDao.addBookmark(bookmark)
    }
}