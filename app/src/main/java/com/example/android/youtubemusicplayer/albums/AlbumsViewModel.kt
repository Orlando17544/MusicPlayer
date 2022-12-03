package com.example.android.youtubemusicplayer.albums

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.youtubemusicplayer.database.AlbumWithArtist
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.PlaylistWithSongs

class AlbumsViewModel(val database: MusicDatabaseDao,
                      application: Application
) : AndroidViewModel(application) {
    val albumWithArtist : LiveData<List<AlbumWithArtist>> = database.getAlbumWithArtist();
}