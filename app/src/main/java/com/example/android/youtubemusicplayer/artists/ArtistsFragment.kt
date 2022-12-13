package com.example.android.youtubemusicplayer.artists

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.CategorySongsActivity
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.Artist
import com.example.android.youtubemusicplayer.database.MusicDatabase

class ArtistsFragment : Fragment() {

    private lateinit var viewModel: ArtistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_artists, container, false)

        val application = requireNotNull(this.activity).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = ArtistsViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ArtistsViewModel::class.java);

        val adapter = ArtistsAdapter();

        viewModel.artistsWithAlbums.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
            }
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.artists);

        recyclerView.adapter = adapter;

        adapter.onItemSelected = { artist: Artist ->
            val intent = Intent(this.activity, CategorySongsActivity::class.java);
            intent.putExtra("category", artist);
            context?.startActivity(intent);
        }

        return view;
    }
}