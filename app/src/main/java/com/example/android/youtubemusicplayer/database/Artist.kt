package com.example.android.youtubemusicplayer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists_table")
data class Artist (
        @PrimaryKey(autoGenerate = true)
        val artistId: Long = 0,

        var name: String = ""
)