package com.example.android.youtubemusicplayer.database

import androidx.room.Embedded
import androidx.room.Relation

data class GenderWithSongs (
    @Embedded val gender: Gender,
    @Relation(
        parentColumn = "genderId",
        entityColumn = "genderContainerId"
    )
    val songs: List<Song>
    )