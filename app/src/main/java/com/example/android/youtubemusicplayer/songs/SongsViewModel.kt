package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongDatabase
import com.example.android.youtubemusicplayer.database.SongDatabaseDao

class SongsViewModel(val database: SongDatabaseDao,
                     application: Application) : AndroidViewModel(application) {

    val songs = database.getSongs();
    //private var songs: LiveData<List<Song>>? = null;
    //private var songs = MutableLiveData<Song?>()

    init {
        //songs = databaseDao.getSongs();
        //val db: SongDatabase = SongDatabase.getInstance(application)
        //val songDatabaseDao = db.songDatabaseDao;
        /*val databaseDao: SongDatabaseDao;

        songs = databaseDao.getSongs();*/
    }
}