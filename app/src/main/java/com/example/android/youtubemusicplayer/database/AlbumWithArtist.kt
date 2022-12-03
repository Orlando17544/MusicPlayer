package com.example.android.youtubemusicplayer.database

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithArtist (
    @Embedded
    val album: Album,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "albumContainerId"
    )
    val artist: Artist
)