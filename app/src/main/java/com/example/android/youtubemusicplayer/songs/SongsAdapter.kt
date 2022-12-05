package com.example.android.youtubemusicplayer.songs

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
import com.example.android.youtubemusicplayer.database.Playlist
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist

class SongsAdapter : ListAdapter<SongWithAlbumAndArtist, SongsAdapter.SongsViewHolder>(SongsDiffCallback()) {

    lateinit var onOptionsSelected: ((view: View, menuRes: Int, song: Song) -> Unit);

    lateinit var onItemChange: ((View, Int, Int) -> Unit);
    lateinit var onItemSelected: ((SongWithAlbumAndArtist) -> Unit);
    lateinit var onItemDeselected: (() -> Unit);
    var positionSelected = -1;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.song_item, parent, false) as LinearLayout

        return SongsViewHolder(view);
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val item = getItem(position);

        holder.itemView.findViewById<TextView>(R.id.song_artist).text = item?.albumAndArtist?.artist?.name;

        if (item?.song?.name?.length ?: 0 > 20) {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item?.song?.name?.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item?.song?.name;
        }

        holder.itemView.findViewById<LinearLayout>(R.id.clickable_song)
            .setOnClickListener(View.OnClickListener {

            if (positionSelected.equals(position)) {
                positionSelected = -1;

                onItemDeselected?.invoke();
            } else {
                positionSelected = position;

                onItemSelected?.invoke(item);
            }

            notifyDataSetChanged();
        })

        onItemChange?.invoke(holder.itemView, position, positionSelected);

        holder.itemView.findViewById<ImageView>(R.id.song_options)
            .setOnClickListener(View.OnClickListener {
                onOptionsSelected.invoke(it, R.menu.menu_song, item.song)
            })
    }

    class SongsViewHolder(linearLayout: LinearLayout): RecyclerView.ViewHolder(linearLayout) {
        var songName: TextView;
        var songArtist: TextView;

        init {
            songName = linearLayout.findViewById(R.id.song_name);
            songArtist = linearLayout.findViewById(R.id.song_artist);
        }
    }
}

class SongsDiffCallback : DiffUtil.ItemCallback<SongWithAlbumAndArtist>() {
    override fun areItemsTheSame(oldItem: SongWithAlbumAndArtist, newItem: SongWithAlbumAndArtist): Boolean {
        if (oldItem.song.songId == newItem.song.songId
            && oldItem.albumAndArtist?.artist?.artistId == newItem.albumAndArtist?.artist?.artistId) {
            return true;
        }
        return false;
    }

    override fun areContentsTheSame(oldItem: SongWithAlbumAndArtist, newItem: SongWithAlbumAndArtist): Boolean {
        return oldItem == newItem;
    }
}