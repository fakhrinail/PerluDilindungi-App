package com.tubes.gapedulidilindungi.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addBookmark(bookmark: BookmarkData)

    @Query("DELETE FROM bookmark_table WHERE id = :id")
    fun deleteBookmark(id: Int)

    @Query("SELECT * FROM bookmark_table")
    fun readAllData(): LiveData<List<BookmarkData>>

    @Query("SELECT EXISTS(SELECT * FROM bookmark_table WHERE id = :id)")
    fun isBookmarkExist(id: Int) : Boolean
}