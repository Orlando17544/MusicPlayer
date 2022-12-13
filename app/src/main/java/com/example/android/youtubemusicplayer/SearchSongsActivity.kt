package com.example.android.youtubemusicplayer

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.MenuRes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.database.*
import com.example.android.youtubemusicplayer.songs.SongsAdapter
import com.example.android.youtubemusicplayer.songs.SongsEditFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class SearchSongsActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchSongsViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchEditText = findViewById<TextInputEditText>(R.id.search_edit_text);
        val backImageView = findViewById<ImageView>(R.id.back);

        backImageView.setOnClickListener(View.OnClickListener {
            this.finish();
        })

        searchEditText.requestFocus();

        val application = requireNotNull(this).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = SearchSongsViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SearchSongsViewModel::class.java);

        val adapter = SongsAdapter();

        searchEditText.doOnTextChanged { text, start, before, count ->
            viewModel.getSongsWithAlbumAndArtistByName(text.toString()).observe(this, androidx.lifecycle.Observer {
                adapter.submitList(it);
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

        val playerIconImageView = findViewById<ImageView>(R.id.player_icon);

        adapter.onItemSelected = { songWithAlbumAndArtist: SongWithAlbumAndArtist ->
            MusicPlayer.playSong(songWithAlbumAndArtist);

            MusicPlayer.mediaplayer.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                playerIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                adapter.positionSelected = -1;
                adapter.notifyDataSetChanged();
            })

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
            } else if (MusicPlayer.isPlaying()) {
                MusicPlayer.pauseSong();
                playerIconImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                MusicPlayer.playSong();
                playerIconImageView.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        })

        MusicPlayer?.currentSongWithAlbumAndArtist?.let { currentSongWithAlbumAndArtist ->
            viewModel.database.getSongWithAlbumAndArtist().observe(this, androidx.lifecycle.Observer { songsWithAlbumAndArtist ->
                if (MusicPlayer.isPlaying()) {
                    val index = songsWithAlbumAndArtist.indexOf(currentSongWithAlbumAndArtist);

                    if (index != -1) {
                        adapter.positionSelected = index;
                        adapter.notifyDataSetChanged();
                    }
                }
            })
        }

        MusicPlayer?.currentSongWithAlbumAndArtist?.let {
            val playerLinearLayout = findViewById<LinearLayout>(R.id.player_item);

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

                    adapter.positionSelected = -1;
                    adapter.notifyDataSetChanged();
                })
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
}