package com.example.android.youtubemusicplayer.songs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.*
import com.google.android.material.snackbar.Snackbar

class SongsEditFragment : DialogFragment() {

    private lateinit var viewModel: SongsEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_songs_edit, container, false);

        val songWithAlbumAndArtistSelected = arguments?.getParcelable<SongWithAlbumAndArtist?>("songWithAlbumAndArtist");

        val application = requireNotNull(this.activity).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = SongsEditViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SongsEditViewModel::class.java);

        val imageViewClose = view.findViewById<ImageView>(R.id.close_image_view);
        val buttonCancel = view.findViewById<Button>(R.id.cancel_button);
        val buttonOk = view.findViewById<Button>(R.id.ok_button);

        imageViewClose.setOnClickListener(View.OnClickListener {
            dismiss();
            Snackbar.make(this.requireView(), "The song was not changed", Snackbar.LENGTH_SHORT).show();
        })

        buttonCancel.setOnClickListener(View.OnClickListener {
            dismiss();
            Snackbar.make(this.requireView(), "The song was not changed", Snackbar.LENGTH_SHORT).show();
        })

        val selectPlaylistAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.select_playlist);

        songWithAlbumAndArtistSelected?.song?.songId?.let {
            viewModel.getPlaylistNameBySongId(it).observe(viewLifecycleOwner, Observer {
                selectPlaylistAutoComplete.setText(it);
                viewModel.playlistsNames.observe(viewLifecycleOwner, Observer {
                    val adapterPlaylist = ArrayAdapter(requireContext(), R.layout.playlist_item_autocomplete, it);
                    selectPlaylistAutoComplete.setAdapter(adapterPlaylist);
                })
            })
        }

        val selectAlbumAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.select_album);

        songWithAlbumAndArtistSelected?.song?.songId?.let {
            viewModel.getAlbumNameBySongId(it).observe(viewLifecycleOwner, Observer {
                selectAlbumAutoComplete.setText(it);
                songWithAlbumAndArtistSelected?.albumAndArtist?.artist?.artistId?.let {
                    viewModel.getAlbumsNamesByArtistId(it).observe(viewLifecycleOwner, Observer {
                        val adapterAlbum  = ArrayAdapter(requireContext(), R.layout.album_item_autocomplete, it);
                        selectAlbumAutoComplete.setAdapter(adapterAlbum);
                    })
                }
            })
        }

        val selectGenreAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.select_genre);

        songWithAlbumAndArtistSelected?.song?.songId?.let {
            viewModel.getGenreNameBySongId(it).observe(viewLifecycleOwner, Observer {
                selectGenreAutoComplete.setText(it);
                viewModel.genresNames.observe(viewLifecycleOwner, Observer {
                    val adapterGenre = ArrayAdapter(requireContext(), R.layout.genre_item_autocomplete, it)
                    selectGenreAutoComplete.setAdapter(adapterGenre);
                })
            })
        }

        buttonOk.setOnClickListener(View.OnClickListener {
            dismiss();
            songWithAlbumAndArtistSelected?.let {
                viewModel.updateSong(
                    it, selectPlaylistAutoComplete.text.toString(),
                    selectAlbumAutoComplete.text.toString(),
                    selectGenreAutoComplete.text.toString())
            }
        })

        return view;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}