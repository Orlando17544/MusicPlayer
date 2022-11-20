package com.example.android.youtubemusicplayer.download_music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R

class DownloadableSongsAdapter(val data: Array<DownloadableSong>, val activityViewModel: ViewModel) : RecyclerView.Adapter<DownloadableSongsAdapter.DownloadableSongsViewHolder>() {

    var onItemChange: ((View, Int, List<Int>) -> Unit)? = null
    val positionsSelected = mutableListOf<Int>();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadableSongsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.song_item, parent, false) as LinearLayout

        return DownloadableSongsViewHolder(view);
    }

    override fun onBindViewHolder(holder: DownloadableSongsViewHolder, position: Int) {
        val item = data.get(position);

        holder.itemView.findViewById<TextView>(R.id.song_artist).text = item.artist;

        if (item.name.length > 20) {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item.name.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item.name;
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (activityViewModel is DownloadMusicViewModel) {
                if (positionsSelected.contains(position)) {
                    positionsSelected.remove(position);
                    activityViewModel.downloadableSongsSelected.remove(item);
                } else {
                    positionsSelected.add(position);
                    activityViewModel.downloadableSongsSelected.add(item);
                }

                notifyItemChanged(position);
            } else {
                println("tro")
            }
        })

        onItemChange?.invoke(holder.itemView, position, positionsSelected);
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    class DownloadableSongsViewHolder(linearLayout: LinearLayout): RecyclerView.ViewHolder(linearLayout) {
        var songName: TextView;
        var songArtist: TextView;

        init {
            songName = linearLayout.findViewById(R.id.song_name);
            songArtist = linearLayout.findViewById(R.id.song_artist);
        }
    }
}