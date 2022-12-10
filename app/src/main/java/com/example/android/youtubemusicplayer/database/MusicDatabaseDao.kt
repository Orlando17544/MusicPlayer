package com.example.android.youtubemusicplayer.database

import android.widget.ArrayAdapter
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

    @Query("SELECT * FROM songs_table WHERE playlistContainerId = :playlistId")
    suspend fun getSongsByPlaylistId(playlistId: Long): List<Song>

    @Query("SELECT * FROM songs_table WHERE albumContainerId = :albumId")
    suspend fun getSongsByAlbumId(albumId: Long): List<Song>

    @Query("SELECT * FROM songs_table " +
            "INNER JOIN albums_table ON albumId = albumContainerId " +
            "WHERE artistContainerId = :artistId")
    suspend fun getSongsByArtistId(artistId: Long): List<Song>

    @Transaction
    @Query("SELECT * FROM songs_table")
    fun getSongWithAlbumAndArtist(): LiveData<List<SongWithAlbumAndArtist>>

    @Transaction
    @Query("SELECT * FROM songs_table WHERE playlistContainerId = :playlistId")
    suspend fun getSongWithAlbumAndArtistByPlaylistId(playlistId: Long): List<SongWithAlbumAndArtist>

    @Insert
    suspend fun insertPlaylist(playlist: Playlist)

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Query("SELECT name FROM playlists_table")
    fun getPlaylistsNames(): LiveData<List<String>>;

    @Query("SELECT * FROM playlists_table WHERE name = :playlistName")
    suspend fun getPlaylistByName(playlistName: String): Playlist;

    @Query("SELECT A.[name] FROM playlists_table A " +
            "INNER JOIN songs_table ON playlistContainerId = playlistId " +
            "WHERE songId = :songId")
    suspend fun getPlaylistNameBySongId(songId: Long): String;

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

    @Query("SELECT name FROM artists_table")
    fun getArtistsNamesLive(): LiveData<List<String>>

    @Query("SELECT name FROM artists_table")
    suspend fun getArtistsNames(): List<String>

    @Query("SELECT * FROM artists_table WHERE name = :name")
    suspend fun getArtistByName(name: String): Artist

    @Query("SELECT * FROM artists_table WHERE artistId = :id")
    suspend fun getArtistById(id: Long): Artist

    @Insert
    suspend fun insertAlbum(album: Album): Long

    @Update
    suspend fun updateAlbum(album: Album)

    @Delete
    suspend fun deleteAlbum(album: Album)

    @Query("SELECT * FROM albums_table WHERE name = :albumName")
    suspend fun getAlbumByName(albumName: String): Album;

    @Query("SELECT * FROM albums_table WHERE artistContainerId = :artistId")
    suspend fun getAlbumsByArtistId(artistId: Long): List<Album>

    @Query("SELECT name FROM albums_table WHERE artistContainerId = :artistId")
    suspend fun getAlbumsNamesByArtistId(artistId: Long): List<String>

    @Query("SELECT A.[name] FROM albums_table A " +
            "INNER JOIN songs_table ON albumContainerId = albumId " +
            "WHERE songId = :songId")
    suspend fun getAlbumNameBySongId(songId: Long): String

    @Insert
    suspend fun insertGenre(genre: Genre): Long

    @Update
    suspend fun updateGenre(genre: Genre)

    @Delete
    suspend fun deleteGenre(genre: Genre)

    @Query("SELECT * FROM genres_table WHERE name = :genreName")
    suspend fun getGenreByName(genreName: String): Genre;

    @Query("SELECT name FROM genres_table")
    fun getGenresNames(): LiveData<List<String>>;

    @Query("SELECT A.[name] FROM genres_table A " +
            "INNER JOIN songs_table ON genreContainerId = genreId " +
            "WHERE songId = :songId")
    suspend fun getGenreNameBySongId(songId: Long): String

    @Transaction
    @Query("SELECT * FROM genres_table")
    fun getGenresWithSongs(): LiveData<List<GenreWithSongs>>

    @Query("SELECT * FROM songs_table WHERE genreContainerId = :genreId")
    suspend fun getSongsByGenreId(genreId: Long): List<Song>
}