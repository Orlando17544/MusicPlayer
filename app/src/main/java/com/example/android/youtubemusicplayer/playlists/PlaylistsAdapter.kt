package com.example.android.youtubemusicplayer.playlists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.database.PlaylistWithSongs
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.download_music.DownloadableSongsAdapter
import com.example.android.youtubemusicplayer.songs.SongsDiffCallback

class PlaylistsAdapter: ListAdapter<PlaylistWithSongs, PlaylistsAdapter.PlaylistsViewHolder>(PlaylistsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.playlist_item, parent, false) as LinearLayout

        return PlaylistsViewHolder(view);
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val item = getItem(position);

        holder.itemView.findViewById<TextView>(R.id.playlist_name).text = item?.playlist?.name;

        if (item?.playlist?.name?.length ?: 0 > 20) {
            holder.itemView.findViewById<TextView>(R.id.playlist_name).text = item?.playlist?.name?.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.playlist_name).text = item?.playlist?.name;
        }

        holder.itemView.findViewById<LinearLayout>(R.id.clickable_playlist)
            .setOnClickListener(View.OnClickListener {
                print("algo")
            })
    }

    class PlaylistsViewHolder(linearLayout: LinearLayout): RecyclerView.ViewHolder(linearLayout) {
        var playlistName: TextView;
        var numberSongsPlaylist: TextView;

        init {
            playlistName = linearLayout.findViewById(R.id.playlist_name);
            numberSongsPlaylist = linearLayout.findViewById(R.id.number_songs_playlist);
        }
    }
}

class PlaylistsDiffCallback : DiffUtil.ItemCallback<PlaylistWithSongs>() {
    override fun areItemsTheSame(oldItem: PlaylistWithSongs, newItem: PlaylistWithSongs): Boolean {
        return oldItem.playlist.playlistId == newItem.playlist.playlistId;
    }

    override fun areContentsTheSame(
        oldItem: PlaylistWithSongs,
        newItem: PlaylistWithSongs
    ): Boolean {
        return oldItem == newItem;
    }

}