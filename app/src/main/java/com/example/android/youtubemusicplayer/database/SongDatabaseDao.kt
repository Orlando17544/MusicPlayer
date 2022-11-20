package com.example.android.youtubemusicplayer.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SongDatabaseDao {
    @Insert
    suspend fun insert(song: Song)

    @Update
    suspend fun update(song: Song)

    @Delete
    suspend fun delete(song: Song)

    @Query("SELECT * from songs_table")
    fun getSongs(): LiveData<List<Song>>

    @Query("SELECT DISTINCT playlist from songs_table ")
    fun getPlaylists(): LiveData<List<String>>

    @Query("SELECT DISTINCT album from songs_table")
    fun getAlbums(): LiveData<List<String>>

    @Query("SELECT DISTINCT artist from songs_table")
    fun getArtists(): LiveData<List<String>>

    @Query("SELECT DISTINCT gender from songs_table")
    fun getGenders(): LiveData<List<String>>

    @Query("SELECT * from songs_table WHERE playlist = :playlist")
    fun getSongsPlaylist(playlist: String): LiveData<List<Song>>

    @Query("SELECT * from songs_table WHERE album = :album")
    fun getSongsAlbum(album: String): LiveData<List<Song>>

    @Query("SELECT * from songs_table WHERE artist = :artist")
    fun getSongsArtist(artist: String): LiveData<List<Song>>

    @Query("SELECT * from songs_table WHERE gender = :gender")
    fun getSongsGender(gender: String): LiveData<List<Song>>
}