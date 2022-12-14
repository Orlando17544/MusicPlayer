package com.example.android.youtubemusicplayer

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.database.*
import com.example.android.youtubemusicplayer.download_music.DownloadableSong
import com.example.android.youtubemusicplayer.network.Api
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import okhttp3.Dispatcher
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.net.URL

class MainViewModel(
    val database: MusicDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    fun onDownload(downloadableSongsSelectedParcelable: Array<Parcelable>) {

        val downloadableSongsSelected =
            convertToDownloadableSong(downloadableSongsSelectedParcelable);
        val storage = Firebase.storage

        for (downloadableSongSelected in downloadableSongsSelected) {
            // Create a reference to a file from a Google Cloud Storage URI
            val gsReference = storage.getReferenceFromUrl(downloadableSongSelected.uri);

            gsReference.downloadUrl.addOnSuccessListener {
                // Got the download URL for 'users/me/profile.png'
                print(it.toString());
                viewModelScope.launch {
                    try {
                        val responseBody = withContext(Dispatchers.IO) {
                            Api.retrofitService.downloadFile(it.toString())
                                .body()
                        }
                        val fileDirectory = generateDirectory(downloadableSongSelected);
                        val path = fileDirectory.absolutePath;

                        saveFile(responseBody, path);
                        insert(downloadableSongSelected, path);
                    } catch (e: Exception) {
                        Log.e("error", "Failure: ${e.message}");
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

    suspend fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String) {
        withContext(Dispatchers.IO) {
            lateinit var input: InputStream

            body?.let {
                try {
                    input = body.byteStream();

                    val fos = FileOutputStream(pathWhereYouWantToSaveFile)
                    fos.use {
                        val buffer = ByteArray(4 * 1024) // or other buffer size
                        var read: Int
                        while (input.read(buffer).also { read = it } != -1) {
                            it.write(buffer, 0, read)
                        }
                        it.flush()
                    }
                    Log.e("Success", "");
                } catch (e: Exception) {
                    Log.e("saveFile", e.toString())
                } finally {
                    input?.close()
                }
            }
        }
    }

    val mutex = Mutex();

    private suspend fun insert(downloadableSong: DownloadableSong, path: String) {
        withContext(Dispatchers.IO) {

            mutex.withLock {
                val artists = database.getArtistsNames();

                val newSong: Song = Song();

                val artistNameList = artists.filter { it == downloadableSong.artist };

                if (artistNameList.size == 0) {
                    val newArtist: Artist = Artist();

                    newArtist.name = downloadableSong.artist;

                    val newArtistId = database.insertArtist(newArtist);

                    val newAlbum: Album = Album();

                    newAlbum.name = "Album of " + downloadableSong.artist;
                    newAlbum.artistContainerId = newArtistId;

                    val newAlbumId = database.insertAlbum(newAlbum);

                    newSong.albumContainerId = newAlbumId;
                } else if (artistNameList.size == 1) {
                    val artistName = artistNameList.get(0);
                    val artist = database.getArtistByName(artistName);

                    newSong.albumContainerId = database.getAlbumsByArtistId(artist.artistId).get(0).albumId;
                }

                newSong.path = path;
                newSong.name = downloadableSong.name;
                newSong.playlistContainerId = 0;

                database.insertSong(newSong);
            }
        }
    }

    private fun convertToDownloadableSong(downloadableSongsSelectedParcelable: Array<Parcelable>): MutableList<DownloadableSong> {
        var downloadableSongsSelected = mutableListOf<DownloadableSong>()

        for (downloadableSongSelectedParcelable in downloadableSongsSelectedParcelable) {
            downloadableSongsSelected.add(downloadableSongSelectedParcelable as DownloadableSong);
        }
        return downloadableSongsSelected;
    }
}