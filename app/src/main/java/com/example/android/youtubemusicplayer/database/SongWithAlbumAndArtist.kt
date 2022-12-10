package com.example.android.youtubemusicplayer.database

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongWithAlbumAndArtist (
        @Embedded
        val song: Song,
        @Relation(
                entity = Album::class,
                parentColumn = "albumContainerId",
                entityColumn = "artistContainerId"
        )
        val albumAndArtist: AlbumAndArtist
        ) : Parcelable