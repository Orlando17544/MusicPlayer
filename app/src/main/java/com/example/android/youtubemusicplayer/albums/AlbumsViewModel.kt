package com.example.android.youtubemusicplayer.albums

import android.app.Application
import androidx.lifecycle.*
import com.example.android.youtubemusicplayer.database.*
import kotlinx.coroutines.launch

class AlbumsViewModel(
    val database: MusicDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    val albumAndArtist: LiveData<List<AlbumAndArtist>> = database.getAlbumAndArtist();
    val artists: LiveData<List<String>> = database.getArtistsNamesLive();

    fun addAlbum(albumName: String, artistName: String) {
        viewModelScope.launch {
            val artist = database.getArtistByName(artistName);

            val newAlbum = Album();

            newAlbum.name = albumName;
            newAlbum.artistContainerId = artist.artistId;

            database.insertAlbum(newAlbum);
        }
    }

    fun updateAlbum(album: Album, albumName: String, artistName: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>();

        viewModelScope.launch {
            val albumsOfArtist = database.getAlbumsByArtistId(album.artistContainerId);

            // If there is only album, don't delete it
            if (albumsOfArtist.size == 1 && artistName != database.getArtistById(album.artistContainerId).name) {
                result.value = false;
                return@launch
            }

            val artist = database.getArtistByName(artistName);

            album.name = albumName;
            album.artistContainerId = artist.artistId;

            database.updateAlbum(album);

            result.value = true;
        }

        return result;
    }

    fun deleteAlbum(album: Album): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        viewModelScope.launch {

            val albumsOfArtist = database.getAlbumsByArtistId(album.artistContainerId);

            // If there is only album, don't delete it
            if (albumsOfArtist.size == 1) {
                result.value = false;
                return@launch
            }

            var newAlbumIdSongs: Long = 0;
            for (albumOfArtist in albumsOfArtist) {
                if (!albumOfArtist.equals(album)) {
                    newAlbumIdSongs = albumOfArtist.albumId;
                }
            }

            val songs: List<Song> = database.getSongsByAlbumId(album.albumId);

            for (song in songs) {
                song.albumContainerId = newAlbumIdSongs;

                database.updateSong(song);
            }
            database.deleteAlbum(album);

            result.value = true;
        }
        return result;
    }
}
