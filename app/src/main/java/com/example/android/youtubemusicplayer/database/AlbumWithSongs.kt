package com.example.android.youtubemusicplayer.database

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithSongs (
    @Embedded val album: Album,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "albumContainerId"
    )
    val songs: List<Song>
    )