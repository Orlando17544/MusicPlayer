package com.example.android.youtubemusicplayer.playlists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.MenuRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.database.PlaylistWithSongs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class PlaylistFragment : Fragment() {

    private lateinit var viewModel: PlaylistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_playlist, container, false);

        val application = requireNotNull(this.activity).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = PlaylistsViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(PlaylistsViewModel::class.java);

        val adapter = PlaylistsAdapter(context);

        viewModel.playlistWithSongs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
            }
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.playlists);

        recyclerView.adapter = adapter;

        val addPlaylist = view.findViewById<ImageView>(R.id.add_playlist);

        addPlaylist.setOnClickListener(View.OnClickListener {
            val addPlaylistView = inflater.inflate(R.layout.add_edit_playlist, null)

            MaterialAlertDialogBuilder(it.context)
                .setView(addPlaylistView)
                .setNegativeButton("Cancel") { dialog, which ->
                    Snackbar.make(it, "The playlist was not added", Snackbar.LENGTH_SHORT).show();
                }
                .setPositiveButton("Ok") { dialog, which ->
                    val newPlaylistEditText = addPlaylistView.findViewById<TextInputEditText>(R.id.edit_playlist_name);

                    if (newPlaylistEditText.text?.length?.equals(0)!!) {
                        Snackbar.make(this.requireView(), "The playlist wasn't added because it can't be empty", Snackbar.LENGTH_SHORT).show();
                    } else {
                        viewModel.addPlaylist(newPlaylistEditText.text.toString());
                    }
                }
                .show();
        })

        adapter.onItemSelected = { playlistId: Long ->
            val intent = Intent(this.activity, PlaylistSongsActivity::class.java);
            intent.putExtra("playlistId", playlistId);
            context?.startActivity(intent);
        }

        adapter.onOptionsSelected = { view: View, menuRes: Int, playlist: Playlist ->
            showMenu(view, R.menu.menu_playlist, playlist);
        }

        return view;
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int, playlist: Playlist) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_playlist -> {
                    val editPlaylistView = layoutInflater.inflate(R.layout.add_edit_playlist, null)

                    editPlaylistView.findViewById<EditText>(R.id.edit_playlist_name).setText(playlist.name);

                    MaterialAlertDialogBuilder(view.context)
                        .setView(editPlaylistView)
                        .setNegativeButton("Cancel") { dialog, which ->
                            Snackbar.make(view, "The playlist was not changed", Snackbar.LENGTH_SHORT).show();
                        }
                        .setPositiveButton("Ok") { dialog, which ->
                            val newPlaylistEditText = editPlaylistView.findViewById<TextInputEditText>(R.id.edit_playlist_name);

                            if (newPlaylistEditText.text?.length?.equals(0)!!) {
                                Snackbar.make(this.requireView(), "The playlist didn't change because it can't be empty", Snackbar.LENGTH_SHORT).show();
                            } else {
                                viewModel.updatePlaylist(playlist, newPlaylistEditText.text.toString());
                            }
                        }
                        .show();
                }
                R.id.delete_playlist -> {
                    viewModel.deletePlaylist(playlist);
                }
            }
            false
        }

        // Show the popup menu.
        popup.show()
    }
}