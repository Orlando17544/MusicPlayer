package com.example.android.youtubemusicplayer.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao

class SongsEditViewModel(val database: MusicDatabaseDao,
                         application: Application
) : AndroidViewModel(application) {

}