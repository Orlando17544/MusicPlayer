package com.example.android.youtubemusicplayer.genres

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.GenreWithSongs
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.Genre
import com.example.android.youtubemusicplayer.database.Song
import kotlinx.coroutines.launch

class GenresViewModel(val database: MusicDatabaseDao,
                      application: Application
) : AndroidViewModel(application) {
    val genresWithSongs : LiveData<List<GenreWithSongs>> = database.getGenresWithSongs();

    fun addGenre(genreName: String) {
        val newGenre = Genre();
        newGenre.name = genreName;

        viewModelScope.launch {
            database.insertGenre(newGenre);
        }
    }

    fun deleteGenre(genre: Genre) {

        viewModelScope.launch {
            val songs: List<Song> = database.getSongsByGenreId(genre.genreId);

            for (song in songs) {
                song.genreContainerId = 0;

                database.updateSong(song);
            }

            database.deleteGenre(genre);
        }
    }

    fun updateGenre(genre: Genre, genreName: String) {
        viewModelScope.launch {
            genre.name = genreName;
            database.updateGenre(genre);
        }
    }
}