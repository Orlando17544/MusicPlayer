package com.example.android.youtubemusicplayer.playlists

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.MusicPlayer
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import com.example.android.youtubemusicplayer.songs.SongsAdapter
import com.example.android.youtubemusicplayer.songs.SongsEditFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar

class PlaylistSongsActivity : AppCompatActivity() {

    private lateinit var viewModel: PlaylistSongsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_songs)

        val playlist = intent.extras?.getParcelable<Playlist>("playlist");

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            finish();
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_songs -> {
                    // Handle favorite icon press
                    // Start another activity
                    true
                }

                else -> false
            }
        }

        topAppBar.title = playlist?.name;

        val application = requireNotNull(this).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = PlaylistSongsViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(PlaylistSongsViewModel::class.java);

        val adapter = SongsAdapter();

        playlist?.playlistId?.let {
            viewModel.getSongsWithAlbumAndArtistByPlaylistId(it).observe(this, Observer {
                it?.let {
                    adapter.submitList(it);
                }
            })
        }

        adapter.onItemChange = { itemView: View, position: Int, positionSelected: Int ->
            if (positionSelected.equals(position)) {
                itemView.setBackgroundColor(Color.parseColor("#ecaf31"));
                itemView.findViewById<ImageView>(R.id.song_icon)
                    .setBackgroundColor(Color.parseColor("#ecaf31"));
            } else {
                itemView.setBackgroundColor(Color.parseColor("#202755"));
                itemView.findViewById<ImageView>(R.id.song_icon)
                    .setBackgroundColor(Color.parseColor("#373c66"));
            }
        }

        adapter.onItemSelected = { songWithAlbumAndArtist: SongWithAlbumAndArtist ->
            MusicPlayer.playSong(songWithAlbumAndArtist);

            val playerLinearLayout = findViewById<LinearLayout>(R.id.player_item);

            val nameTextView = playerLinearLayout?.findViewById<TextView>(R.id.song_name);
            val artistTextView = playerLinearLayout?.findViewById<TextView>(R.id.song_artist);

            nameTextView?.text = songWithAlbumAndArtist.song.name;
            artistTextView?.text = songWithAlbumAndArtist.albumAndArtist?.artist?.name;

            val playerIconImageView = playerLinearLayout?.findViewById<ImageView>(R.id.player_icon);

            playerIconImageView?.setImageResource(R.drawable.ic_baseline_pause_24);
        }

        adapter.onItemDeselected = {
            val playerIconImageView = findViewById<ImageView>(R.id.player_icon);

            MusicPlayer.pauseSong();
            playerIconImageView?.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }

        adapter.onOptionsSelected =
            { view: View, menuRes: Int, songWithAlbumAndArtist: SongWithAlbumAndArtist ->
                showMenu(view, R.menu.menu_song, songWithAlbumAndArtist);
            }

        val recyclerView: RecyclerView = findViewById(R.id.songs);

        recyclerView.adapter = adapter;

        val playerLinearLayout = findViewById<LinearLayout>(R.id.player_item);

        playerLinearLayout.setOnClickListener(View.OnClickListener {
            val playerIconImageView = findViewById<ImageView>(R.id.player_icon);

            if (MusicPlayer.currentSongWithAlbumAndArtist == null) {
                Snackbar.make(it, "You need to select a song", Snackbar.LENGTH_SHORT).show();
            } else if (MusicPlayer.paused) {
                MusicPlayer.playSong();
                playerIconImageView.setImageResource(R.drawable.ic_baseline_pause_24);
            } else {
                MusicPlayer.pauseSong();
                playerIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            }
        })

        MusicPlayer?.currentSongWithAlbumAndArtist?.let { currentSongWithAlbumAndArtist ->
            playlist?.playlistId?.let {
                viewModel.getSongsWithAlbumAndArtistByPlaylistId(it).observe(this, Observer { songsWithAlbumAndArtist ->
                    val index = songsWithAlbumAndArtist.indexOf(currentSongWithAlbumAndArtist);

                    adapter.positionSelected = index;
                    adapter.notifyDataSetChanged();
                })
            }
        }

        MusicPlayer?.currentSongWithAlbumAndArtist?.let {
            val playerLinearLayout = findViewById<LinearLayout>(R.id.player_item);

            val songNameTextView = playerLinearLayout.findViewById<TextView>(R.id.song_name);
            val songArtistTextView = playerLinearLayout.findViewById<TextView>(R.id.song_artist);

            songNameTextView.text = it?.song?.name;
            songArtistTextView.text = it?.albumAndArtist?.artist?.name;

            val playerIconImageView = findViewById<ImageView>(R.id.player_icon);

            if (MusicPlayer.paused) {
                playerIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                playerIconImageView.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        }
    }

    private fun showMenu(
        view: View,
        @MenuRes menuRes: Int,
        songWithAlbumAndArtist: SongWithAlbumAndArtist
    ) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_song -> {
                    showDialog(songWithAlbumAndArtist);
                }
                R.id.delete_song -> {
                    viewModel.deleteSong(songWithAlbumAndArtist);
                }
            }
            false
        }

        // Show the popup menu.
        popup.show()
    }

    fun showDialog(songWithAlbumAndArtist: SongWithAlbumAndArtist) {
        val fragmentManager = supportFragmentManager
        val newFragment = SongsEditFragment()

        val args = Bundle();
        args.putParcelable("songWithAlbumAndArtist", songWithAlbumAndArtist);

        newFragment.arguments = args;

        // The device is smaller, so show the fragment fullscreen
        val transaction = fragmentManager?.beginTransaction()
        // For a little polish, specify a transition animation
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction
            ?.add(android.R.id.content, newFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onResume() {
        super.onResume()

    }
}