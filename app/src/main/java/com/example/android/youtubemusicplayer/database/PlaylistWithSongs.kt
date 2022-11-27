package com.example.android.youtubemusicplayer.database

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistWithSongs (
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "playlistContainerId"
    )
    val songs: List<Song>
    )