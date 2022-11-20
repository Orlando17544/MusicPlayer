package com.example.android.youtubemusicplayer

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.youtubemusicplayer.download_music.DownloadableSong
import com.example.android.youtubemusicplayer.network.Api
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    fun downloadMusicFiles(downloadableSongsSelectedParcelable: Array<Parcelable>) {

        val downloadableSongsSelected = mutableListOf<DownloadableSong>();

        for (downloadableSongSelectedParcelable in downloadableSongsSelectedParcelable) {
            downloadableSongsSelected.add(downloadableSongSelectedParcelable as DownloadableSong);
        }

        val storage = Firebase.storage

        for (downloadableSongSelected in downloadableSongsSelected) {
            // Create a reference to a file from a Google Cloud Storage URI
            val gsReference = storage.getReferenceFromUrl(downloadableSongSelected.uri);

            gsReference.downloadUrl.addOnSuccessListener { uri ->
                // Got the download URL for 'users/me/profile.png'
                viewModelScope.launch {
                    try {
                        val cw = ContextWrapper(getApplication<Application?>().applicationContext)
                        // path to /data/data/yourapp/app_data/songs
                        val directory = cw.getDir("songs", Context.MODE_PRIVATE)
                        val mypath = File(directory, downloadableSongSelected.name + ".mp3")

                        val responseBody = Api.retrofitService.downloadFile(uri.toString()).body();
                        val result = saveFile(responseBody, mypath.absolutePath)
                        Log.e("Ruta: ", result);
                    } catch (e: Exception) {
                        Log.e("error","Failure: ${e.message}");
                    }
                }
            }.addOnFailureListener { exception ->
                // Handle any errors
                Log.e("Error: ", exception.message as String)
            }
        }
    }

    suspend fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String) : String {
        if (body==null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
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
}