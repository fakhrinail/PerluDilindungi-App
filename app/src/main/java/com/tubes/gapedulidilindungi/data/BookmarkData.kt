package com.tubes.gapedulidilindungi.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "bookmark_table")
data class BookmarkData (
    @PrimaryKey
    val kodeFaskes: String,
    val namaFaskes: String?,
    val alamatFaskes: String?,
    val noTelpFaskes: String?,
    val jenisFaskes: String?,
    val statusFaskes: String?
): Parcelable

@Parcelize
data class ListBookmarkData (
    var listFaskes: List<BookmarkData>
    ) : Parcelable