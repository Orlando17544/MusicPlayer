package com.example.android.youtubemusicplayer.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "albums_table")
data class Album (
    @PrimaryKey(autoGenerate = true)
    val albumId: Long = 0,

    var name: String = "",

    @ColumnInfo(name = "artistContainerId")
    var artistContainerId: Long = 0
) : Parcelable