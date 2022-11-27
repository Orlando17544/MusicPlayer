package com.example.android.youtubemusicplayer.playlists

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.songs.SongsViewModel

class PlaylistsViewModelFactory(private val dataSource: MusicDatabaseDao,
                                private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistsViewModel::class.java)) {
            return PlaylistsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}