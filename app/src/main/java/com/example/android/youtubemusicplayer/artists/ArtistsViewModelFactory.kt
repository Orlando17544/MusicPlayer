package com.example.android.youtubemusicplayer.artists

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.youtubemusicplayer.albums.AlbumsViewModel
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao

class ArtistsViewModelFactory(private val dataSource: MusicDatabaseDao,
                              private val application: Application
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistsViewModel::class.java)) {
            return ArtistsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}