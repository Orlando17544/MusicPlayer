package com.example.android.youtubemusicplayer.playlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.database.PlaylistWithSongs
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao

class PlaylistsViewModel(val database: MusicDatabaseDao,
                         application: Application) : AndroidViewModel(application) {
    val playlistWithSongs : LiveData<List<PlaylistWithSongs>> = database.getPlaylistsWithSongs();
}
