package com.example.android.youtubemusicplayer.genders

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.MusicPlayer
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.songs.SongsAdapter
import com.example.android.youtubemusicplayer.songs.SongsViewModel
import com.example.android.youtubemusicplayer.songs.SongsViewModelFactory

class GendersFragment : Fragment() {

    private lateinit var viewModel: GendersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_genders, container, false);

        val application = requireNotNull(this.activity).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = GendersViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(GendersViewModel::class.java);

        val adapter = GendersAdapter();

        viewModel.gendersWithSongs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
            }
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.genders);

        recyclerView.adapter = adapter;

        return view;
    }
}