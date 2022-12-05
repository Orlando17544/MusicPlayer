package com.example.android.youtubemusicplayer.database

import androidx.room.Embedded
import androidx.room.Relation

data class SongWithAlbumAndArtist (
        @Embedded
        val song: Song,
        @Relation(
                entity = Album::class,
                parentColumn = "albumContainerId",
                entityColumn = "artistContainerId"
        )
        val albumAndArtist: AlbumAndArtist?
        )