package com.example.android.youtubemusicplayer.songs

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.MusicPlayer
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import com.google.android.material.snackbar.Snackbar

class SongsFragment : Fragment() {

    lateinit var playerIconImageView: ImageView;
    lateinit var adapter: SongsAdapter;

    private lateinit var viewModel: SongsViewModel;

    public var variable = "";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_songs, container, false)

        val application = requireNotNull(this.activity).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = SongsViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SongsViewModel::class.java);

        adapter = SongsAdapter();

        viewModel.songsWithAlbumAndArtist.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
            }
        })

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

        val playerLinearLayout = activity?.findViewById<LinearLayout>(R.id.player_item);

        playerIconImageView = playerLinearLayout?.findViewById<ImageView>(R.id.player_icon)!!;

        adapter.onItemSelected = { songWithAlbumAndArtist: SongWithAlbumAndArtist ->
            MusicPlayer.playSong(songWithAlbumAndArtist);

            MusicPlayer.mediaplayer.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                playerIconImageView?.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                adapter.positionSelected = -1;
                adapter.notifyDataSetChanged();
            })

            val nameTextView = playerLinearLayout?.findViewById<TextView>(R.id.song_name);
            val artistTextView = playerLinearLayout?.findViewById<TextView>(R.id.song_artist);

            nameTextView?.text = songWithAlbumAndArtist.song.name;
            artistTextView?.text = songWithAlbumAndArtist.albumAndArtist?.artist?.name;

            playerIconImageView?.setImageResource(R.drawable.ic_baseline_pause_24);
        }

        adapter.onItemDeselected = {
            val playerIconImageView = activity?.findViewById<ImageView>(R.id.player_icon);

            MusicPlayer.pauseSong();
            playerIconImageView?.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }

        adapter.onOptionsSelected = { view: View, menuRes: Int, songWithAlbumAndArtist: SongWithAlbumAndArtist ->
            showMenu(view, R.menu.menu_song, songWithAlbumAndArtist);
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.songs);

        recyclerView.adapter = adapter;

        // Inflate the layout for this fragment
        return view;
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int, songWithAlbumAndArtist: SongWithAlbumAndArtist) {
        val popup = PopupMenu(requireContext(), view)
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
        val fragmentManager = parentFragmentManager
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
        MusicPlayer?.currentSongWithAlbumAndArtist?.let { currentSongWithAlbumAndArtist ->

            viewModel.songsWithAlbumAndArtist.observe(viewLifecycleOwner, Observer { songsWithAlbumAndArtist ->
                if (MusicPlayer.isPlaying()) {
                    val index = songsWithAlbumAndArtist.indexOf(currentSongWithAlbumAndArtist);

                    adapter.positionSelected = index;
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.positionSelected = -1;
                    adapter.notifyDataSetChanged();
                }
            })
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
}