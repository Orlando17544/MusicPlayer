package com.example.android.youtubemusicplayer.genres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.GenreWithSongs
import com.example.android.youtubemusicplayer.database.Genre

class GenresAdapter: ListAdapter<GenreWithSongs, GenresAdapter.GenresViewHolder>(
    GenresDiffCallback()
) {
    lateinit var onOptionsSelected: ((view: View, menuRes: Int, genre: Genre) -> Unit);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.genre_item, parent, false) as LinearLayout

        return GenresViewHolder(view);
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val item = getItem(position);

        holder.itemView.findViewById<TextView>(R.id.genre_name).text = item?.genre?.name;
        holder.itemView.findViewById<TextView>(R.id.number_songs_genre).text = item?.songs?.size.toString();

        if (item.songs.size == 1) {
            holder.itemView.findViewById<TextView>(R.id.number_songs_genre).text = item.songs.size.toString() + " song";
        } else {
            holder.itemView.findViewById<TextView>(R.id.number_songs_genre).text = item.songs.size.toString() + " songs";
        }

        if (item?.genre?.name?.length ?: 0 > 20) {
            holder.itemView.findViewById<TextView>(R.id.genre_name).text = item?.genre?.name?.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.genre_name).text = item?.genre?.name;
        }

        holder.itemView.findViewById<LinearLayout>(R.id.clickable_genre)
            .setOnClickListener(View.OnClickListener {
                print("algo")
            })

        holder.itemView.findViewById<ImageView>(R.id.genre_options)
            .setOnClickListener(View.OnClickListener {
                onOptionsSelected.invoke(it, R.menu.menu_genre, item.genre)
            })
    }

    class GenresViewHolder(linearLayout: LinearLayout): RecyclerView.ViewHolder(linearLayout) {
        var genreName: TextView;
        var numberSongsGenre: TextView;

        init {
            genreName = linearLayout.findViewById(R.id.genre_name);
            numberSongsGenre = linearLayout.findViewById(R.id.number_songs_genre);
        }
    }
}

class GenresDiffCallback : DiffUtil.ItemCallback<GenreWithSongs>() {
    override fun areItemsTheSame(oldItem: GenreWithSongs, newItem: GenreWithSongs): Boolean {
        if (oldItem.genre.genreId == newItem.genre.genreId
            && oldItem.songs.size == newItem.songs.size) {
            return true;
        }
        return false;
    }

    override fun areContentsTheSame(
        oldItem: GenreWithSongs,
        newItem: GenreWithSongs
    ): Boolean {
        return oldItem == newItem;
    }
}