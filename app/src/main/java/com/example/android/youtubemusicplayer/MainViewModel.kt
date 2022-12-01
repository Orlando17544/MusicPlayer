package com.example.android.youtubemusicplayer

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.MusicDatabaseDao
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.download_music.DownloadableSong
import com.example.android.youtubemusicplayer.network.Api
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainViewModel(val database: MusicDatabaseDao,
                    application: Application) : AndroidViewModel(application) {
    fun onDownload(downloadableSongsSelectedParcelable: Array<Parcelable>) {

        val downloadableSongsSelected = convertToDownloadableSong(downloadableSongsSelectedParcelable);
        val storage = Firebase.storage

        for (downloadableSongSelected in downloadableSongsSelected) {
            // Create a reference to a file from a Google Cloud Storage URI
            val gsReference = storage.getReferenceFromUrl(downloadableSongSelected.uri);

            gsReference.downloadUrl.addOnSuccessListener {
                // Got the download URL for 'users/me/profile.png'
                viewModelScope.launch {
                    try {
                        val responseBody = Api.retrofitService.downloadFile(it.toString()).body();
                        val fileDirectory = generateDirectory(downloadableSongSelected);
                        val path = fileDirectory.absolutePath;

                        val result = saveFile(responseBody, path);
                        insert(downloadableSongSelected, path);
                    } catch (e: Exception) {
                        Log.e("error","Failure: ${e.message}");
                    }
                }
            }.addOnFailureListener {
                // Handle any errors
                Log.e("Error: ", it.message as String)
            }
        }
    }

    suspend fun generateDirectory(downloadableSongSelected: DownloadableSong): File {
        val cw = ContextWrapper(getApplication<Application?>().applicationContext)
        // path to /data/data/yourapp/app_data/songs
        val directory = cw.getDir("songs", Context.MODE_PRIVATE)
        return File(directory, downloadableSongSelected.name + ".mp3")
    }

    suspend fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String) : String {
        if (body != null) body else ""
        lateinit var input: InputStream
        try {
            input = body!!.byteStream();

            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
            fos.use {
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    it.write(buffer, 0, read)
                }
                it.flush()
            }
            Log.e("sucessssssssssss", "");
            return pathWhereYouWantToSaveFile
        }catch (e:Exception){
            Log.e("saveFile",e.toString())
        }
        finally {
            input?.close()
        }
        return ""
    }

    private suspend fun insert(downloadableSong: DownloadableSong, path: String) {
        lateinit var newSong: Song;

        newSong = Song();
        newSong.path = path;
        newSong.name = downloadableSong.name;
        newSong.artist = downloadableSong.artist;
        newSong.playlistContainerId = 0;

        database.insertSong(newSong);
    }

    private fun convertToDownloadableSong(downloadableSongsSelectedParcelable: Array<Parcelable>): MutableList<DownloadableSong> {
        var downloadableSongsSelected = mutableListOf<DownloadableSong>()

        for (downloadableSongSelectedParcelable in downloadableSongsSelectedParcelable) {
            downloadableSongsSelected.add(downloadableSongSelectedParcelable as DownloadableSong);
        }
        return downloadableSongsSelected;
    }
}