package com.example.android.youtubemusicplayer.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase

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

        return view;
    }
}