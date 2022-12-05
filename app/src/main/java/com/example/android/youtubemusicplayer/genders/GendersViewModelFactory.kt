package com.example.android.youtubemusicplayer.genders

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.songs.SongsViewModel

class GendersViewModelFactory(private val dataSource: MusicDatabaseDao,
                              private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GendersViewModel::class.java)) {
            return GendersViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}