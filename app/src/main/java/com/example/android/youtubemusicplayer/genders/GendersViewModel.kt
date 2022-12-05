package com.example.android.youtubemusicplayer.genders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.youtubemusicplayer.database.GenderWithSongs
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao

class GendersViewModel(val database: MusicDatabaseDao,
                       application: Application
) : AndroidViewModel(application) {
    val gendersWithSongs : LiveData<List<GenderWithSongs>> = database.getGendersWithSongs();
}