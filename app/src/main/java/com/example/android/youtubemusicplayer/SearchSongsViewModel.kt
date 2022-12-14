package com.example.android.youtubemusicplayer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import kotlinx.coroutines.launch
import java.io.File

class SearchSongsViewModel(val database: MusicDatabaseDao,
                           application: Application
) : AndroidViewModel(application) {

    fun getSongsWithAlbumAndArtistByName(search: String): LiveData<List<SongWithAlbumAndArtist>> {
        val result = MutableLiveData<List<SongWithAlbumAndArtist>>();

        viewModelScope.launch {
            val songsWithAlbumAndArtist = database.getSongsWithAlbumAndArtistByName(search);

            result.value = songsWithAlbumAndArtist;
        }

        return result;
    }

    fun deleteSong(songWithAlbumAndArtist: SongWithAlbumAndArtist) {
        val song = songWithAlbumAndArtist?.song;
        val artist = songWithAlbumAndArtist.albumAndArtist?.artist;

        viewModelScope.launch {
            if (songWithAlbumAndArtist.albumAndArtist?.artist?.artistId?.let {
                    database.getSongsByArtistId(
                        it
                    ).size.equals(1)
                } == true) {
                artist?.let { database.deleteArtist(it) };

                val albums = artist?.artistId?.let { database.getAlbumsByArtistId(it) };

                if (albums != null) {
                    for (album in albums) {
                        database.deleteAlbum(album);
                    }
                }
            }

            val file = File(song?.path);
            file.delete();

            song?.let { database.deleteSong(it) };
        }
    }
}