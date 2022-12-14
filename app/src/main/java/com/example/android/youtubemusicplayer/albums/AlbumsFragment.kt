package com.example.android.youtubemusicplayer.albums

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.CategorySongsActivity
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.Album
import com.example.android.youtubemusicplayer.database.AlbumAndArtist
import com.example.android.youtubemusicplayer.database.MusicDatabase
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

        viewModel.albumAndArtist.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
            }
        })

        val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false);

        val recyclerView: RecyclerView = view.findViewById(R.id.albums);

        recyclerView.adapter = adapter;
        recyclerView.layoutManager = manager;

        viewModel = ViewModelProvider(this).get(AlbumsViewModel::class.java)

        val addAlbum = view.findViewById<ImageView>(R.id.add_album);

        addAlbum.setOnClickListener(View.OnClickListener {
            val addAlbumView = inflater.inflate(R.layout.add_edit_album, null)

            val items = viewModel.artists.observe(viewLifecycleOwner, Observer {
                val adapter = ArrayAdapter(requireContext(), R.layout.artist_item_autocomplete, it)
                val autoCompleteTextView = addAlbumView.findViewById<AutoCompleteTextView>(R.id.edit_artist_name);
                autoCompleteTextView.setAdapter(adapter)
            })

            MaterialAlertDialogBuilder(it.context)
                .setView(addAlbumView)
                .setNegativeButton("Cancel") { dialog, which ->
                    Snackbar.make(it, "The album was not added", Snackbar.LENGTH_SHORT).show();
                }
                .setPositiveButton("Ok") { dialog, which ->
                    val newAlbumNameEditText = addAlbumView.findViewById<TextInputEditText>(R.id.edit_album_name);
                    val newArtistNameAutoComplete = addAlbumView.findViewById<AutoCompleteTextView>(R.id.edit_artist_name);

                    if (newAlbumNameEditText.text?.length?.equals(0)!!) {
                        Snackbar.make(this.requireView(), "The album wasn't added because it can't be empty", Snackbar.LENGTH_SHORT).show();
                    } else {
                        viewModel.addAlbum(newAlbumNameEditText.text.toString(), newArtistNameAutoComplete.text.toString());
                    }
                }
                .show();
        })

        adapter.onItemSelected = { album: Album ->
            val intent = Intent(this.activity, CategorySongsActivity::class.java);
            intent.putExtra("category", album);
            context?.startActivity(intent);
        }

        adapter.onOptionsSelected = { view: View, menuRes: Int, albumAndArtist: AlbumAndArtist ->
            showMenu(view, R.menu.menu_album, albumAndArtist);
        }

        return view;
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int, albumAndArtist: AlbumAndArtist) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_album -> {
                    val editAlbumView = layoutInflater.inflate(R.layout.add_edit_album, null)

                    editAlbumView.findViewById<EditText>(R.id.edit_album_name).setText(albumAndArtist.album.name);

                    val items = viewModel.artists.observe(viewLifecycleOwner, Observer {
                        val adapter = ArrayAdapter(requireContext(), R.layout.artist_item_autocomplete, it)
                        val autoCompleteTextView = editAlbumView.findViewById<AutoCompleteTextView>(R.id.edit_artist_name);
                        autoCompleteTextView.setText(albumAndArtist.artist?.name);
                        autoCompleteTextView.setAdapter(adapter);
                    })

                    MaterialAlertDialogBuilder(view.context)
                        .setView(editAlbumView)
                        .setNegativeButton("Cancel") { dialog, which ->
                            Snackbar.make(view, "The album was not changed", Snackbar.LENGTH_SHORT).show();
                        }
                        .setPositiveButton("Ok") { dialog, which ->
                            val newAlbumNameEditText = editAlbumView.findViewById<TextInputEditText>(R.id.edit_album_name);
                            val newArtistNameAutoComplete = editAlbumView.findViewById<AutoCompleteTextView>(R.id.edit_artist_name);

                            if (newAlbumNameEditText.text?.length?.equals(0)!!) {
                                Snackbar.make(this.requireView(), "The album didn't change because it can't be empty", Snackbar.LENGTH_SHORT).show();
                            } else {
                                viewModel.updateAlbum(albumAndArtist.album, newAlbumNameEditText.text.toString(), newArtistNameAutoComplete.text.toString())
                                    .observe(viewLifecycleOwner, Observer { isAlbumEdited ->
                                        if (!isAlbumEdited) {
                                            Snackbar.make(view, "There must be at least one album per artist", Snackbar.LENGTH_SHORT).show();
                                        }
                                    });
                            }
                        }
                        .show();
                }
                R.id.delete_album -> {
                    viewModel.deleteAlbum(albumAndArtist.album).observe(viewLifecycleOwner, Observer { isAlbumDeleted ->
                        if (!isAlbumDeleted) {
                            Snackbar.make(view, "There must be at least one album per artist", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                }
            }
            false
        }

        // Show the popup menu.
        popup.show()
    }
}
