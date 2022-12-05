package com.example.android.youtubemusicplayer.albums

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
import com.example.android.youtubemusicplayer.database.Album
import com.example.android.youtubemusicplayer.database.AlbumAndArtist

class AlbumsAdapter: ListAdapter<AlbumAndArtist, AlbumsAdapter.AlbumsViewHolder>(
    AlbumsDiffCallback()
) {
    lateinit var onOptionsSelected: ((view: View, menuRes: Int, album: Album) -> Unit);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.album_item, parent, false) as LinearLayout

        return AlbumsViewHolder(view);
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        val item = getItem(position);

        holder.itemView.findViewById<TextView>(R.id.album_name).text = item.album.name
        holder.itemView.findViewById<TextView>(R.id.artist_name).text = item.artist?.name;

        if (item?.album?.name?.length ?: 0 > 15) {
            holder.itemView.findViewById<TextView>(R.id.album_name).text = item?.album?.name?.substring(0, 15) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.album_name).text = item?.album?.name;
        }

        if (item?.artist?.name?.length ?: 0 > 15) {
            holder.itemView.findViewById<TextView>(R.id.artist_name).text = item?.artist?.name?.substring(0, 15) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.artist_name).text = item?.artist?.name;
        }

        /*
        holder.itemView.findViewById<LinearLayout>(R.id.clickable_playlist)
            .setOnClickListener(View.OnClickListener {
                print("algo")
            })


         */

        holder.itemView.findViewById<ImageView>(R.id.album_options)
            .setOnClickListener(View.OnClickListener {
                onOptionsSelected.invoke(it, R.menu.menu_playlist, item.album)
            })
    }

    class AlbumsViewHolder(linearLayout: LinearLayout): RecyclerView.ViewHolder(linearLayout) {
        var albumName: TextView;
        var artistName: TextView;

        init {
            albumName = linearLayout.findViewById(R.id.album_name);
            artistName = linearLayout.findViewById(R.id.artist_name);
        }
    }
}

class AlbumsDiffCallback : DiffUtil.ItemCallback<AlbumAndArtist>() {
    override fun areItemsTheSame(oldItem: AlbumAndArtist, newItem: AlbumAndArtist): Boolean {
        if (oldItem.album.albumId == newItem.album.albumId
            && oldItem.artist?.artistId == newItem.artist?.artistId
        ) {
            return true;
        }
        return false;
    }

    override fun areContentsTheSame(
        oldItem: AlbumAndArtist,
        newItem: AlbumAndArtist
    ): Boolean {
        return oldItem == newItem;
    }
}