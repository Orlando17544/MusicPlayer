package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import kotlinx.coroutines.launch
import java.io.File

class SongsViewModel(
    val database: MusicDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    val songsWithAlbumAndArtist: LiveData<List<SongWithAlbumAndArtist>> =
        database.getSongWithAlbumAndArtist();

    fun deleteSong(songWithAlbumAndArtist: SongWithAlbumAndArtist) {
        val song = songWithAlbumAndArtist.song;
        val artist = songWithAlbumAndArtist.albumAndArtist?.artist;

        viewModelScope.launch {
            if (songWithAlbumAndArtist.albumAndArtist?.artist?.artistId?.let {
                    database.getSongsByArtistId(
                        it
                    ).size.equals(1)
                } == true
            ) {
                artist?.let { database.deleteArtist(it) };

                val albums = artist?.artistId?.let { database.getAlbumsByArtistId(it) };

                if (albums != null) {
                    for (album in albums) {
                        database.deleteAlbum(album);
                    }
                }
            }

            val file = File(song.path);
            file.delete();

            database.deleteSong(song);
        }
    }
}