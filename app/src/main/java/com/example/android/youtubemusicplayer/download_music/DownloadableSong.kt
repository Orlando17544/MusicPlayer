package com.example.android.youtubemusicplayer.download_music

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DownloadableSong(val uri: String, val name: String, val artist: String) : Parcelable