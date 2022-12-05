package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist

class SongsViewModel(val database: MusicDatabaseDao,
                     application: Application) : AndroidViewModel(application) {

    val songAndArtist : LiveData<List<SongWithAlbumAndArtist>> = database.getSongWithAlbumAndArtist();
}