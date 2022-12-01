package com.example.android.youtubemusicplayer.playlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.database.PlaylistWithSongs
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import kotlinx.coroutines.launch

class PlaylistsViewModel(val database: MusicDatabaseDao,
                         application: Application) : AndroidViewModel(application) {
    val playlistWithSongs : LiveData<List<PlaylistWithSongs>> = database.getPlaylistsWithSongs();

    fun addPlaylist(playlistName: String) {
        val newPlaylist = Playlist();
        newPlaylist.name = playlistName;

        viewModelScope.launch {
            database.insertPlaylist(newPlaylist);
        }
    }

    fun deletePlaylist(playlist: Playlist) {

        viewModelScope.launch {
            val songs: List<Song> = database.getSongsByPlaylistId(playlist.playlistId);

            for (song in songs) {
                song.playlistContainerId = 0;

                database.updateSong(song);
            }

            database.deletePlaylist(playlist);
        }
    }

    fun updatePlaylist(playlist: Playlist, playlistName: String) {
        viewModelScope.launch {
            playlist.name = playlistName;
            database.updatePlaylist(playlist);
        }
    }
}
