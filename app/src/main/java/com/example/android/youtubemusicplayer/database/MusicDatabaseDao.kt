package com.example.android.youtubemusicplayer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MusicDatabaseDao {
    @Insert
    suspend fun insertSong(song: Song)

    @Update
    suspend fun updateSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("SELECT * FROM songs_table")
    fun getSongs(): LiveData<List<Song>>

    @Insert
    suspend fun insertPlaylist(playlist: Playlist)

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylists(): List<Playlist>

    @Query("SELECT * FROM playlists_table WHERE name = :name")
    suspend fun getPlaylistByName(name: String): Playlist

    @Transaction
    @Query("SELECT * FROM playlists_table")
    fun getPlaylistsWithSongs(): LiveData<List<PlaylistWithSongs>>
}