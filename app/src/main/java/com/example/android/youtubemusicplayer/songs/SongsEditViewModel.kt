package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import kotlinx.coroutines.launch

class SongsEditViewModel(val database: MusicDatabaseDao,
                         application: Application
) : AndroidViewModel(application) {
    var playlistsNames: LiveData<List<String>> = database.getPlaylistsNames();
    var genresNames: LiveData<List<String>> = database.getGenresNames();

    fun getPlaylistNameBySongId(songId: Long?): LiveData<String> {
        val result = MutableLiveData<String>();

        viewModelScope.launch {
            val playlistName = database.getPlaylistNameBySongId(songId);

            result.value = playlistName;
        }
        return result;
    }

    fun getAlbumNameBySongId(songId: Long?): LiveData<String> {
        val result = MutableLiveData<String>();

        viewModelScope.launch {
            val albumName = database.getAlbumNameBySongId(songId);

            result.value = albumName;
        }
        return result;
    }

    fun getGenreNameBySongId(songId: Long?): LiveData<String> {
        val result = MutableLiveData<String>();

        viewModelScope.launch {
            val genreName = database.getGenreNameBySongId(songId);

            result.value = genreName;
        }
        return result;
    }

    fun getAlbumsNamesByArtistId(artistId: Long?): LiveData<List<String>> {
        val result = MutableLiveData<List<String>>();
        viewModelScope.launch {
            val albumsNames = database.getAlbumsNamesByArtistId(artistId);

            result.value = albumsNames;
        }
        return result;
    }

    fun updateSong(songWithAlbumAndArtist: SongWithAlbumAndArtist?, playlistName: String, albumName: String, genreName: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>();

        if (playlistName.length.equals(0) || albumName.length.equals(0) || genreName.length.equals(0)) {
            result.value = false;

            return result;
        }
        val song = songWithAlbumAndArtist?.song;

        viewModelScope.launch {
            song?.playlistContainerId = database.getPlaylistByName(playlistName).playlistId;
            song?.albumContainerId = database.getAlbumByName(albumName).albumId;
            song?.genreContainerId = database.getGenreByName(genreName).genreId;

            database.updateSong(song);
            result.value = true;
        }
        return result;
    }

    fun deleteSong(songWithAlbumAndArtist: SongWithAlbumAndArtist) {
        val song = songWithAlbumAndArtist?.song;

        viewModelScope.launch {
            database.deleteSong(song);
        }
    }
}