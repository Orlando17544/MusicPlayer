package com.example.android.youtubemusicplayer.database

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumAndArtist (
    @Embedded
    val album: Album,
    @Relation(
        parentColumn = "artistContainerId",
        entityColumn = "artistId"
    )
    val artist: Artist?
)