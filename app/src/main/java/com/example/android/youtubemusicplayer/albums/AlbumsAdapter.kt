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
import com.example.android.youtubemusicplayer.database.AlbumWithArtist

class AlbumsAdapter: ListAdapter<AlbumWithArtist, AlbumsAdapter.AlbumsViewHolder>(
    AlbumsDiffCallback()
) {
    //lateinit var onOptionsSelected: ((view: View, menuRes: Int, playlist: Playlist) -> Unit);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.playlist_item, parent, false) as LinearLayout

        return AlbumsViewHolder(view);
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        val item = getItem(position);

        holder.itemView.findViewById<TextView>(R.id.playlist_name).text = item.album.name;
        holder.itemView.findViewById<TextView>(R.id.number_songs_playlist).text = item.artist.name;

        if (item?.album?.name?.length ?: 0 > 20) {
            holder.itemView.findViewById<TextView>(R.id.playlist_name).text = item?.album?.name?.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.playlist_name).text = item?.album?.name;
        }

        holder.itemView.findViewById<LinearLayout>(R.id.clickable_playlist)
            .setOnClickListener(View.OnClickListener {
                print("algo")
            })
        /*
        holder.itemView.findViewById<ImageView>(R.id.playlist_options)
            .setOnClickListener(View.OnClickListener {
                onOptionsSelected.invoke(it, R.menu.menu_playlist, item.playlist)
            })*/
    }

    class AlbumsViewHolder(linearLayout: LinearLayout): RecyclerView.ViewHolder(linearLayout) {
        var playlistName: TextView;
        var numberSongsPlaylist: TextView;

        init {
            playlistName = linearLayout.findViewById(R.id.playlist_name);
            numberSongsPlaylist = linearLayout.findViewById(R.id.number_songs_playlist);
        }
    }
}

class AlbumsDiffCallback : DiffUtil.ItemCallback<AlbumWithArtist>() {
    override fun areItemsTheSame(oldItem: AlbumWithArtist, newItem: AlbumWithArtist): Boolean {
        if (oldItem.album.albumId == newItem.album.albumId
            && oldItem.artist.artistId == newItem.artist.artistId
        ) {
            return true;
        }
        return false;
    }

    override fun areContentsTheSame(
        oldItem: AlbumWithArtist,
        newItem: AlbumWithArtist
    ): Boolean {
        return oldItem == newItem;
    }
}