package com.example.android.youtubemusicplayer.songs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.MusicPlayer
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class SongsFragment : Fragment() {

    private lateinit var viewModel: SongsViewModel

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

        val adapter = SongsAdapter();

        viewModel.songWithAlbumAndArtist.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
            }
        })

        adapter.onItemChange = { itemView: View, position: Int, positionSelected: Int ->
            if (positionSelected.equals(position)) {
                itemView.setBackgroundColor(Color.parseColor("#ecaf31"));
                itemView.findViewById<ImageView>(R.id.song_icon).setBackgroundColor(Color.parseColor("#ecaf31"));
            } else {
                itemView.setBackgroundColor(Color.parseColor("#202755"));
                itemView.findViewById<ImageView>(R.id.song_icon).setBackgroundColor(Color.parseColor("#373c66"));
            }
        }

        adapter.onItemSelected = { songWithAlbumAndArtist: SongWithAlbumAndArtist ->
            MusicPlayer.playSong(songWithAlbumAndArtist.song);

            val playerLinearLayout = activity?.findViewById<LinearLayout>(R.id.player_item);

            val nameTextView = playerLinearLayout?.findViewById<TextView>(R.id.song_name);
            val artistTextView = playerLinearLayout?.findViewById<TextView>(R.id.song_artist);

            nameTextView?.text = songWithAlbumAndArtist.song.name;
            artistTextView?.text = songWithAlbumAndArtist.albumAndArtist.artist?.name;
        }

        adapter.onItemDeselected = {
            MusicPlayer.pauseSong();
        }

        adapter.onOptionsSelected = { view: View, menuRes: Int, song: Song ->
            showMenu(view, R.menu.menu_song, song);
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.songs);

        recyclerView.adapter = adapter;

        // Inflate the layout for this fragment
        return view;
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int, song: Song) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_song -> {
                    /*
                    val editSongView = layoutInflater.inflate(R.layout.add_edit_song, null)

                    editSongView.findViewById<EditText>(R.id.edit_song_name).setText(song.name);

                    MaterialAlertDialogBuilder(view.context)
                        .setView(editSongView)
                        .setNegativeButton("Cancel") { dialog, which ->
                            Snackbar.make(view, "The song was not changed", Snackbar.LENGTH_SHORT).show();
                        }
                        .setPositiveButton("Ok") { dialog, which ->
                            val newSongEditText = editSongView.findViewById<TextInputEditText>(R.id.edit_song_name);
                            viewModel.updateSong(song, newSongEditText.text.toString());
                        }
                        .show();*/
                }
                R.id.delete_song -> {
                    //viewModel.deleteSong(song);
                }
            }
            false
        }

        // Show the popup menu.
        popup.show()
    }
}