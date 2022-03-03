package com.tubes.gapedulidilindungi.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addBookmark(bookmark: BookmarkData)

    @Query("SELECT * FROM bookmark_table")
    fun readAllData(): LiveData<List<BookmarkData>>
}