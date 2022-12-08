package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import kotlinx.coroutines.launch
import java.io.File

class SongsViewModel(val database: MusicDatabaseDao,
                     application: Application) : AndroidViewModel(application) {

    val songWithAlbumAndArtist : LiveData<List<SongWithAlbumAndArtist>> = database.getSongWithAlbumAndArtist();

    fun deleteSong(songWithAlbumAndArtist: SongWithAlbumAndArtist) {
        val song = songWithAlbumAndArtist?.song;
        val artist = songWithAlbumAndArtist.albumAndArtist?.artist;

        viewModelScope.launch {
            if (database.getSongsByArtistId(songWithAlbumAndArtist.albumAndArtist?.artist?.artistId).size.equals(1)) {
                database.deleteArtist(artist);
            }

            val file = File(song?.path);
            file.delete();

            database.deleteSong(song);
        }
    }
}