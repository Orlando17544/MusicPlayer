package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongDatabase
import com.example.android.youtubemusicplayer.database.SongDatabaseDao

class SongsViewModel(val database: SongDatabaseDao,
                     application: Application) : AndroidViewModel(application) {

    val songs : LiveData<List<Song>> = database.getSongs();
}