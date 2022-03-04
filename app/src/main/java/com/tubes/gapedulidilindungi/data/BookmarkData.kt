package com.tubes.gapedulidilindungi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_table")
data class BookmarkData (
    @PrimaryKey
    val kodeFaskes: String,
    val namaFaskes: String,
    val alamatFaskes: String,
    val noTelpFaskes: String,
    val jenisFaskes: String,
    val statusFaskes: String
)