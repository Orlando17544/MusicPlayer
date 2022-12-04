package com.example.android.youtubemusicplayer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs_table")
data class Song (
    @PrimaryKey(autoGenerate = true)
    var songId: Long = 0,

    @ColumnInfo(name = "path")
    var path: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "artist")
    var artist: String = "",

    @ColumnInfo(name = "playlistContainerId")
    var playlistContainerId: Long = 0,

    @ColumnInfo(name = "albumContainerId")
    var albumContainerId: Long = 0
)