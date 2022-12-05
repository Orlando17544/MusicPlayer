package com.example.android.youtubemusicplayer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres_table")
data class Genre (
    @PrimaryKey(autoGenerate = true)
    val genreId: Long = 0,

    var name: String = "",
)