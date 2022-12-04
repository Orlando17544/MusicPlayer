package com.example.android.youtubemusicplayer.artists

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
import com.example.android.youtubemusicplayer.albums.AlbumsDiffCallback
import com.example.android.youtubemusicplayer.database.Album
import com.example.android.youtubemusicplayer.database.ArtistWithAlbums
import com.example.android.youtubemusicplayer.database.PlaylistWithSongs

class ArtistsAdapter: ListAdapter<ArtistWithAlbums, ArtistsAdapter.ArtistsViewHolder>(
    ArtistsDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.artist_item, parent, false) as LinearLayout

        return ArtistsViewHolder(view);
    }

    override fun onBindViewHolder(holder: ArtistsViewHolder, position: Int) {
        val item = getItem(position);

        holder.itemView.findViewById<TextView>(R.id.artist_name).text = item.artist.name
        if (item.albums.size == 1) {
            holder.itemView.findViewById<TextView>(R.id.number_albums_artist).text = item.albums.size.toString() + " album";
        } else {
            holder.itemView.findViewById<TextView>(R.id.number_albums_artist).text = item.albums.size.toString() + " albums";
        }



        if (item?.artist?.name?.length ?: 0 > 20) {
            holder.itemView.findViewById<TextView>(R.id.artist_name).text = item?.artist?.name?.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.artist_name).text = item?.artist?.name;
        }

        /*
        holder.itemView.findViewById<LinearLayout>(R.id.clickable_playlist)
            .setOnClickListener(View.OnClickListener {
                print("algo")
            })
         */
    }

    class ArtistsViewHolder(linearLayout: LinearLayout): RecyclerView.ViewHolder(linearLayout) {
        var artistName: TextView;
        var numberAlbumsArtist: TextView;

        init {
            artistName = linearLayout.findViewById(R.id.artist_name);
            numberAlbumsArtist = linearLayout.findViewById(R.id.number_albums_artist);
        }
    }
}

class ArtistsDiffCallback : DiffUtil.ItemCallback<ArtistWithAlbums>() {
    override fun areItemsTheSame(oldItem: ArtistWithAlbums, newItem: ArtistWithAlbums): Boolean {
        if (oldItem.artist.artistId == newItem.artist.artistId
            && oldItem.albums.size == newItem.albums.size) {
            return true;
        }
        return false;
    }

    override fun areContentsTheSame(
        oldItem: ArtistWithAlbums,
        newItem: ArtistWithAlbums
    ): Boolean {
        return oldItem == newItem;
    }
}