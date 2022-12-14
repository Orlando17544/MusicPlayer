package com.example.android.youtubemusicplayer

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.download_music.DownloadMusicActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var playerLinearLayout: LinearLayout;

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenSplash.setKeepOnScreenCondition{ false }

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
                R.id.search_songs -> {
                    val intent = Intent(this@MainActivity, SearchSongsActivity::class.java)
                    startActivity(intent);
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

        playerLinearLayout = findViewById<LinearLayout>(R.id.player_item);

        playerLinearLayout.setOnClickListener(View.OnClickListener {
            val playerIconImageView = findViewById<ImageView>(R.id.player_icon);

            if (MusicPlayer.currentSongWithAlbumAndArtist == null) {
                Snackbar.make(it, "You need to select a song", Snackbar.LENGTH_SHORT).show();
            } else if (MusicPlayer.isPlaying()) {
                MusicPlayer.pauseSong();
                playerIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                MusicPlayer.playSong();
                playerIconImageView.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        })
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
        if (it.resultCode == RESULT_OK) {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cm.registerDefaultNetworkCallback(object: ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)

                        val songsToDownload : Array<Parcelable> =
                            it.data?.extras?.getParcelableArray("songsToDownload") as Array<Parcelable>;

                        viewModel.onDownload(songsToDownload);
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        Snackbar.make(findViewById(R.id.root), "You disconnected from internet", Snackbar.LENGTH_SHORT).show();
                    }
                })
            } else {
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

                if (isConnected) {
                    val songsToDownload : Array<Parcelable> =
                        it.data?.extras?.getParcelableArray("songsToDownload") as Array<Parcelable>;

                    viewModel.onDownload(songsToDownload);
                } else {
                    Snackbar.make(this.findViewById(R.id.root), "There is no internet connection ", Snackbar.LENGTH_SHORT).show();
                    return@ActivityResultCallback;
                }
            }
        }
    })

    override fun onResume() {
        super.onResume()

        val playerIconImageView = findViewById<ImageView>(R.id.player_icon);

        MusicPlayer?.currentSongWithAlbumAndArtist?.let {
            val songNameTextView = playerLinearLayout.findViewById<TextView>(R.id.song_name);
            val songArtistTextView = playerLinearLayout.findViewById<TextView>(R.id.song_artist);

            songNameTextView.text = it?.song?.name;
            songArtistTextView.text = it?.albumAndArtist?.artist?.name;

            if (MusicPlayer.isPlaying()) {
                playerIconImageView.setImageResource(R.drawable.ic_baseline_pause_24);
            } else {
                playerIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
        }

        MusicPlayer?.currentSongWithAlbumAndArtist?.let {
            if (MusicPlayer.isPlaying()) {
                MusicPlayer.mediaplayer.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                    playerIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                })
            }
        }

    }
}