package com.example.android.youtubemusicplayer.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.MusicPlayer
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.Song
import kotlin.properties.Delegates

class SongsAdapter : RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {

    var data: List<Song> = listOf<Song>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var onItemChange: ((View, Int, Int) -> Unit);
    lateinit var onItemSelected: ((Song) -> Unit);
    var positionSelected = -1;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.song_item, parent, false) as LinearLayout

        return SongsViewHolder(view);
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val item = data.get(position);

        holder.itemView.findViewById<TextView>(R.id.song_artist).text = item?.artist;

        if (item?.name?.length ?: 0 > 20) {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item?.name?.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item?.name;
        }

        holder.itemView.findViewById<LinearLayout>(R.id.clickable_song)
            .setOnClickListener(View.OnClickListener {

            if (positionSelected.equals(position)) {
                positionSelected = -1;

                MusicPlayer.pauseSong();
            } else {
                positionSelected = position;

                MusicPlayer.playSong(item);

                onItemSelected?.invoke(item);
            }

            notifyDataSetChanged();
        })

        onItemChange?.invoke(holder.itemView, position, positionSelected);
    }

    override fun getItemCount(): Int {
        return data.size;
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