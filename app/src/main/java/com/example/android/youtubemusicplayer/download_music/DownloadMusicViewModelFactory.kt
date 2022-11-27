package com.example.android.youtubemusicplayer.download_music

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.youtubemusicplayer.MainViewModel
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao

class DownloadMusicViewModelFactory(private val dataSource: MusicDatabaseDao,
                                    private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DownloadMusicViewModel::class.java)) {
            return DownloadMusicViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}