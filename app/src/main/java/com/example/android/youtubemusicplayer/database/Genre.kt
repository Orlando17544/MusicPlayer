package com.example.android.youtubemusicplayer.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "genres_table")
data class Genre(
    @PrimaryKey(autoGenerate = true)
    val genreId: Long = 0,

    var name: String = "",
) : Parcelable