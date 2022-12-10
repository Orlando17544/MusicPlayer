package com.example.android.youtubemusicplayer.database

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumAndArtist (
    @Embedded
    val album: Album,
    @Relation(
        parentColumn = "artistContainerId",
        entityColumn = "artistId"
    )
    val artist: Artist
) : Parcelable