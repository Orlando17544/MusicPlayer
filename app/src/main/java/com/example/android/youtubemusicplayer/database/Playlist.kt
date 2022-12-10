package com.example.android.youtubemusicplayer.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "playlists_table")
data class Playlist (
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,

    var name: String = ""
) : Parcelable