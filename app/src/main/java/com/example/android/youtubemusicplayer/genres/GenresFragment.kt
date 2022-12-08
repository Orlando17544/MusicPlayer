package com.example.android.youtubemusicplayer.genres

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
import com.example.android.youtubemusicplayer.database.Genre
import com.example.android.youtubemusicplayer.database.MusicDatabase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class GenresFragment : Fragment() {

    private lateinit var viewModel: GenresViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_genres, container, false);

        val application = requireNotNull(this.activity).application;

        val dataSource = MusicDatabase.getInstance(application).musicDatabaseDao;

        val viewModelFactory = GenresViewModelFactory(dataSource, application);

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(GenresViewModel::class.java);

        val adapter = GenresAdapter();

        viewModel.genresWithSongs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it);
            }
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.genres);

        recyclerView.adapter = adapter;

        val addGenre = view.findViewById<ImageView>(R.id.add_genre);

        addGenre.setOnClickListener(View.OnClickListener {
            val addGenreView = inflater.inflate(R.layout.add_edit_genre, null)

            MaterialAlertDialogBuilder(it.context)
                .setView(addGenreView)
                .setNegativeButton("Cancel") { dialog, which ->
                    Snackbar.make(it, "The genre was not added", Snackbar.LENGTH_SHORT).show();
                }
                .setPositiveButton("Ok") { dialog, which ->
                    val newGenreEditText = addGenreView.findViewById<TextInputEditText>(R.id.edit_genre_name);

                    if (newGenreEditText.text?.length?.equals(0)!!) {
                        Snackbar.make(this.requireView(), "The genre wasn't added because it can't be empty", Snackbar.LENGTH_SHORT).show();
                    } else {
                        viewModel.addGenre(newGenreEditText.text.toString());
                    }
                }
                .show();
        })

        adapter.onOptionsSelected = { view: View, menuRes: Int, genre: Genre ->
            showMenu(view, R.menu.menu_genre, genre);
        }

        return view;
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int, genre: Genre) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_genre -> {
                    val editGenreView = layoutInflater.inflate(R.layout.add_edit_genre, null)

                    editGenreView.findViewById<EditText>(R.id.edit_genre_name).setText(genre.name);

                    MaterialAlertDialogBuilder(view.context)
                        .setView(editGenreView)
                        .setNegativeButton("Cancel") { dialog, which ->
                            Snackbar.make(view, "The genre was not changed", Snackbar.LENGTH_SHORT).show();
                        }
                        .setPositiveButton("Ok") { dialog, which ->
                            val newGenreEditText = editGenreView.findViewById<TextInputEditText>(R.id.edit_genre_name);

                            if (newGenreEditText.text?.length?.equals(0)!!) {
                                Snackbar.make(this.requireView(), "The genre didn't change because it can't be empty", Snackbar.LENGTH_SHORT).show();
                            } else {
                                viewModel.updateGenre(genre, newGenreEditText.text.toString());
                            }
                        }
                        .show();
                }
                R.id.delete_genre -> {
                    viewModel.deleteGenre(genre);
                }
            }
            false
        }

        // Show the popup menu.
        popup.show()
    }
}