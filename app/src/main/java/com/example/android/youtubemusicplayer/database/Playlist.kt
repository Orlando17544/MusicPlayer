package com.example.android.youtubemusicplayer.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class Playlist (
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int = 0,

    val name: String = ""
)