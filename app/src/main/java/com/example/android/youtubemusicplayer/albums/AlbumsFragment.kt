package com.example.android.youtubemusicplayer.albums

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.playlists.PlaylistsAdapter
import com.example.android.youtubemusicplayer.playlists.PlaylistsViewModel
import com.example.android.youtubemusicplayer.playlists.PlaylistsViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class AlbumsFragment : Fragment() {

    private lateinit var viewModel: AlbumsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_albums, container, false);

        val application = requireNotNull(this.activity).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = AlbumsViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(AlbumsViewModel::class.java);

        val adapter = AlbumsAdapter();

        viewModel.albumWithArtist.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
            }
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.albums);

        recyclerView.adapter = adapter;

        viewModel = ViewModelProvider(this).get(AlbumsViewModel::class.java)

        val addPlaylist = view.findViewById<ImageView>(R.id.add_album);

        /*
        addPlaylist.setOnClickListener(View.OnClickListener {
            val addPlaylistView = inflater.inflate(R.layout.add_edit_playlist, null)

            MaterialAlertDialogBuilder(it.context)
                .setView(addPlaylistView)
                .setNegativeButton("Cancel") { dialog, which ->
                    Snackbar.make(it, "The playlist was not added", Snackbar.LENGTH_SHORT).show();
                }
                .setPositiveButton("Ok") { dialog, which ->
                    val newPlaylistEditText = addPlaylistView.findViewById<TextInputEditText>(R.id.edit_playlist_name);
                    viewModel.addPlaylist(newPlaylistEditText.text.toString());
                }
                .show();
        })*/

        /*
        adapter.onOptionsSelected = { view: View, menuRes: Int, playlist: Playlist ->
            showMenu(view, R.menu.menu_playlist, playlist);
        }*/

        return view;
    }
}
