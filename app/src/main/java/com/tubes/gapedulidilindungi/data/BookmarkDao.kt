package com.tubes.gapedulidilindungi.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addBookmark(bookmark: BookmarkData)

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkData)

    @Query("SELECT * FROM bookmark_table")
    fun readAllData(): LiveData<List<BookmarkData>>
}