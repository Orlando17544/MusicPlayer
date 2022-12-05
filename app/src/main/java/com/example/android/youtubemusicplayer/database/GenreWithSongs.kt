package com.example.android.youtubemusicplayer.database

import androidx.room.Embedded
import androidx.room.Relation

data class GenreWithSongs (
    @Embedded val genre: Genre,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "genreContainerId"
    )
    val songs: List<Song>
    )