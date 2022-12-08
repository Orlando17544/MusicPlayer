package com.example.android.youtubemusicplayer

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.download_music.DownloadMusicActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;
        val viewModelFactory = MainViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        viewModel =
            ViewModelProvider(
                this, viewModelFactory).get(MainViewModel::class.java)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        topAppBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.download_music -> {
                    val intent = Intent(this@MainActivity, DownloadMusicActivity::class.java)
                    launcher.launch(intent);
                }
            }
            false
        })

        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager2)

        viewPager2.adapter = CategoriesPagerAdapter(this)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Songs"
                1 -> tab.text = "Playlists"
                2 -> tab.text = "Albums"
                3 -> tab.text = "Artists"
                4 -> tab.text = "Genres"
            }
        }
        tabLayoutMediator.attach()

        val playerLinearLayout = findViewById<LinearLayout>(R.id.player_item);

        playerLinearLayout.setOnClickListener(View.OnClickListener {
            val playerIconImageView = findViewById<ImageView>(R.id.player_icon);

            if (MusicPlayer.currentSong == null) {
                Snackbar.make(it, "You need to select a song", Snackbar.LENGTH_SHORT).show();
            } else if (MusicPlayer.paused) {
                MusicPlayer.playSong();
                playerIconImageView.setImageResource(R.drawable.ic_baseline_pause_24);
            } else {
                MusicPlayer.pauseSong();
                playerIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
        })
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
        if (it.resultCode == RESULT_OK) {
            val songsToDownload : Array<Parcelable> =
                it.data?.extras?.getParcelableArray("songsToDownload") as Array<Parcelable>;

            viewModel.onDownload(songsToDownload);
        }
    })


}