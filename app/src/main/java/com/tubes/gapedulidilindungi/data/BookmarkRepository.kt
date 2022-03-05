package com.tubes.gapedulidilindungi.data

import androidx.lifecycle.LiveData

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    val readAllData: LiveData<List<BookmarkData>> = bookmarkDao.readAllData()

    fun addBookmark(bookmark: BookmarkData) {
        bookmarkDao.addBookmark(bookmark)
    }

    fun deleteBookmark(id: Int) {
        bookmarkDao.deleteBookmark(id)
    }

    fun isBookmarkExist(id: Int) {
        bookmarkDao.isBookmarkExist(id)
    }
}