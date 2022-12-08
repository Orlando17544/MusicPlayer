package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import kotlinx.coroutines.launch

class SongsViewModel(val database: MusicDatabaseDao,
                     application: Application) : AndroidViewModel(application) {

    val songWithAlbumAndArtist : LiveData<List<SongWithAlbumAndArtist>> = database.getSongWithAlbumAndArtist();

    fun deleteSong(songWithAlbumAndArtist: SongWithAlbumAndArtist) {
        val song = songWithAlbumAndArtist.song;

        viewModelScope.launch {
            database.deleteSong(song);
        }
    }
}