package com.example.android.youtubemusicplayer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genders_table")
data class Gender (
    @PrimaryKey(autoGenerate = true)
    val genderId: Long = 0,

    var name: String = "",
)