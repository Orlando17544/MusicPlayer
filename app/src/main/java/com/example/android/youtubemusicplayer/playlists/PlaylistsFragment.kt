package com.example.android.youtubemusicplayer.playlists

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase
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

        val adapter = PlaylistsAdapter();

        viewModel.playlistWithSongs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
                print("Cambio: " + it.size);
            }
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.playlists);

        recyclerView.adapter = adapter;

        viewModel = ViewModelProvider(this).get(PlaylistsViewModel::class.java)

        val addPlaylist = view.findViewById<ImageView>(R.id.add_playlist);

        addPlaylist.setOnClickListener(View.OnClickListener {

            // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
            val builder: AlertDialog.Builder? = activity?.let {
                val builder = AlertDialog.Builder(it);

                val inflater = requireActivity().layoutInflater;

                val addPlaylistView = inflater.inflate(R.layout.add_playlist, null);

                builder.apply {
                    setView(addPlaylistView)
                    setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button

                            val newPlaylistEditText = addPlaylistView.findViewById<TextInputEditText>(R.id.new_playlist_name);
                            viewModel.addPlaylist(newPlaylistEditText.text.toString());
                        })
                    setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
                }
            }

            // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            val dialog: AlertDialog? = builder?.create();

            dialog?.show();
        })

        return view;
    }
}
