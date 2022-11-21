package com.example.android.youtubemusicplayer.songs

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SongsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SongsFragment : Fragment() {

    private lateinit var viewModel: SongsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_songs, container, false)

        val application = requireNotNull(this.activity).application;

        val dataSource = SongDatabase.getInstance(application).songDatabaseDao;

        val viewModelFactory = SongsViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SongsViewModel::class.java);

        val adapter = SongsAdapter();

        viewModel.songs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it;
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

        val recyclerView: RecyclerView = view.findViewById(R.id.songs);

        recyclerView.adapter = adapter;

        // Inflate the layout for this fragment
        return view;
    }
}