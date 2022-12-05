package com.example.android.youtubemusicplayer.download_music

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.songs.SongsAdapter
import com.example.android.youtubemusicplayer.songs.SongsViewModel
import com.example.android.youtubemusicplayer.songs.SongsViewModelFactory


class DownloadMusicActivity : AppCompatActivity() {

    private lateinit var viewModel: DownloadMusicViewModel

    // Storage Permissions
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_music)

        verifyStoragePermissions(this);

        val application = requireNotNull(this).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = DownloadMusicViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DownloadMusicViewModel::class.java);

        val adapter = DownloadableSongsAdapter(viewModel.downloadableSongs, viewModel);

        viewModel.songAndArtist.observe(this, Observer {
            it?.let {
                adapter.data = it;
            }
        })

        //val adapter = DownloadableSongsAdapter(viewModel.downloadableSongs, viewModel);

        adapter.onItemChange = { itemView: View, position: Int,
                                 positionsSelected: List<Int>, positionsDownloaded: List<Int> ->
            if (positionsDownloaded.contains(position)) {
                itemView.setBackgroundColor(Color.parseColor("#004d40"));
                itemView.findViewById<ImageView>(R.id.song_icon).setBackgroundColor(Color.parseColor("#004d40"));
            } else if (positionsSelected.contains(position)) {
                itemView.setBackgroundColor(Color.parseColor("#ecaf31"));
                itemView.findViewById<ImageView>(R.id.song_icon).setBackgroundColor(Color.parseColor("#ecaf31"));
            } else {
                itemView.setBackgroundColor(Color.parseColor("#202755"));
                itemView.findViewById<ImageView>(R.id.song_icon).setBackgroundColor(Color.parseColor("#373c66"));
            }
        }

        val recyclerView: RecyclerView = findViewById(R.id.downloadable_songs);

        recyclerView.adapter = adapter;

        val downloadButton: Button = findViewById(R.id.download_button);

        downloadButton.setOnClickListener(View.OnClickListener {
            val returnIntent = Intent()

            val downloadableSongsSelectedArray : Array<DownloadableSong> = viewModel.downloadableSongsSelected.toTypedArray();
            returnIntent.putExtra("songsToDownload", downloadableSongsSelectedArray);
            setResult(RESULT_OK, returnIntent);
            finish();
        })
    }

    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            if (activity != null) activity else Activity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                if (activity != null) activity else Activity(),
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}