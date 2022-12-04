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

    @Query("SELECT * FROM songs_table WHERE playlistContainerId = :playlistId")
    suspend fun getSongsByPlaylistId(playlistId: Long): List<Song>

    @Query("SELECT * FROM songs_table WHERE albumContainerId = :albumId")
    suspend fun getSongsByAlbumId(albumId: Long): List<Song>

    @Insert
    suspend fun insertPlaylist(playlist: Playlist)

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Transaction
    @Query("SELECT * FROM playlists_table")
    fun getPlaylistsWithSongs(): LiveData<List<PlaylistWithSongs>>

    @Transaction
    @Query("SELECT * FROM artists_table")
    fun getArtistsWithAlbums(): LiveData<List<ArtistWithAlbums>>

    @Transaction
    @Query("SELECT * FROM albums_table")
    fun getAlbumAndArtist(): LiveData<List<AlbumAndArtist>>

    @Insert
    suspend fun insertArtist(artist: Artist): Long

    @Update
    suspend fun updateArtist(artist: Artist)

    @Delete
    suspend fun deleteArtist(artist: Artist)

    suspend @Query("SELECT name FROM artists_table")
    fun getArtistsNames(): List<String>

    suspend @Query("SELECT * FROM artists_table WHERE name = :name")
    fun getArtistByName(name: String): Artist

    suspend @Query("SELECT * FROM artists_table WHERE artistId = :id")
    fun getArtistById(id: Long): Artist

    @Insert
    suspend fun insertAlbum(album: Album): Long

    @Update
    suspend fun updateAlbum(album: Album)

    @Delete
    suspend fun deleteAlbum(album: Album)

    @Query("SELECT * FROM albums_table WHERE artistContainerId = :artistId")
    suspend fun getAlbumsByArtistId(artistId: Long): List<Album>
}