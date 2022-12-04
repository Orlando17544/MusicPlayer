package com.example.android.youtubemusicplayer.artists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao

class ArtistsViewModel(val database: MusicDatabaseDao,
                       application: Application
) : AndroidViewModel(application) {
    val artistsWithAlbums = database.getArtistsWithAlbums();
}