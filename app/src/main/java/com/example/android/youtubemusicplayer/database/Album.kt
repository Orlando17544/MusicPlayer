package com.example.android.youtubemusicplayer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums_table")
data class Album (
    @PrimaryKey(autoGenerate = true)
    val albumId: Int = 0,

    var name: String = "",
)