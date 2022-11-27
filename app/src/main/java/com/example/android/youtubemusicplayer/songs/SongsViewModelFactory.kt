package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao

class SongsViewModelFactory(private val dataSource: MusicDatabaseDao,
                            private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongsViewModel::class.java)) {
            return SongsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}