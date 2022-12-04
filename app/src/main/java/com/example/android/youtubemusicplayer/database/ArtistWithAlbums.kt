package com.example.android.youtubemusicplayer.database

import androidx.room.Embedded
import androidx.room.Relation

data class ArtistWithAlbums (
    @Embedded
    val artist: Artist,
    @Relation(
        parentColumn = "artistId",
        entityColumn = "artistContainerId"
    )
    val albums: List<Album>
)