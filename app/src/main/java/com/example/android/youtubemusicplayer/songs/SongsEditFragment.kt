package com.example.android.youtubemusicplayer.songs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.MusicPlayer
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist
import com.google.android.material.snackbar.Snackbar

class SongsEditFragment : DialogFragment() {

    private lateinit var viewModel: SongsEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_songs_edit, container, false);

        val application = requireNotNull(this.activity).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = SongsEditViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SongsEditViewModel::class.java);

        val imageViewClose = view.findViewById<ImageView>(R.id.close_image_view);
        val buttonCancel = view.findViewById<Button>(R.id.cancel_button);

        imageViewClose.setOnClickListener(View.OnClickListener {
            dismiss();
            Snackbar.make(this.requireView(), "The song was not changed", Snackbar.LENGTH_SHORT).show();
        })

        buttonCancel.setOnClickListener(View.OnClickListener {
            dismiss();
            Snackbar.make(this.requireView(), "The song was not changed", Snackbar.LENGTH_SHORT).show();
        })

        val selectPlaylistAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.select_playlist);

        val itemsPlaylist = listOf("Material", "Design", "Components", "Android")
        val adapterPlaylist = ArrayAdapter(requireContext(), R.layout.playlist_item_autocomplete, itemsPlaylist)
        selectPlaylistAutoComplete.setAdapter(adapterPlaylist)

        val selectAlbumAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.select_album);

        val itemsAlbum = listOf("Material", "Design", "Components", "Android")
        val adapterAlbum = ArrayAdapter(requireContext(), R.layout.album_item_autocomplete, itemsAlbum)
        selectAlbumAutoComplete.setAdapter(adapterAlbum)

        val selectGenreAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.select_genre);

        val itemsGenre = listOf("Material", "Design", "Components", "Android")
        val adapterGenre = ArrayAdapter(requireContext(), R.layout.genre_item_autocomplete, itemsGenre)
        selectGenreAutoComplete.setAdapter(adapterGenre)

        return view;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}