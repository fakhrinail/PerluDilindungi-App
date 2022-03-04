package com.tubes.gapedulidilindungi.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<BookmarkData>>
    private val repository: BookmarkRepository

    init {
        val bookmarkDao = BookmarkDatabase.getDatabase(application).bookmarkDao()
        repository = BookmarkRepository(bookmarkDao)
        readAllData = repository.readAllData
    }

    fun addBookmark(bookmark: BookmarkData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBookmark(bookmark)
        }
    }

    fun deleteBookmark(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBookmark(id)
        }
    }

    fun isBookmarkExist(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.isBookmarkExist(id)
        }
    }
}