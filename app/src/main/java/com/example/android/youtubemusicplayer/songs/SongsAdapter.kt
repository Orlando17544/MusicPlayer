package com.example.android.youtubemusicplayer.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.Song

class SongsAdapter(val data: LiveData<List<Song>>, val activityViewModel: ViewModel) : RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {

    var onItemChange: ((View, Int, Int) -> Unit)? = null
    val positionSelected = -1;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.song_item, parent, false) as LinearLayout

        return SongsViewHolder(view);
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val item = data.value?.get(position);

        holder.itemView.findViewById<TextView>(R.id.song_artist).text = item?.artist;

        if (item?.name?.length!! > 20) {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item.name.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item.name;
        }

        onItemChange?.invoke(holder.itemView, position, positionSelected);
    }

    override fun getItemCount(): Int {
        return data.value!!.size;
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