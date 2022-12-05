package com.example.android.youtubemusicplayer.download_music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.youtubemusicplayer.R
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist

class DownloadableSongsAdapter(val downloadableSongs: Array<DownloadableSong>, val viewModel: DownloadMusicViewModel) : RecyclerView.Adapter<DownloadableSongsAdapter.DownloadableSongsViewHolder>() {

    var data: List<SongWithAlbumAndArtist> = listOf<SongWithAlbumAndArtist>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var onItemChange: ((View, Int, List<Int>, List<Int>) -> Unit);
    val positionsSelected = mutableListOf<Int>();
    val positionsDownloaded = mutableListOf<Int>();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadableSongsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.song_item, parent, false) as LinearLayout

        return DownloadableSongsViewHolder(view);
    }

    override fun onBindViewHolder(holder: DownloadableSongsViewHolder, position: Int) {
        val item = downloadableSongs.get(position);

        holder.itemView.findViewById<TextView>(R.id.song_artist).text = item.artist;

        if (item.name.length > 20) {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item.name.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.song_name).text = item.name;
        }

        val downloadedIdentifiers = data.map {
            it.song.name + it.albumAndArtist.artist?.name;
        }

        val downloadableIdentifier = item.name + item.artist;

        if (downloadedIdentifiers.contains(downloadableIdentifier) && !positionsDownloaded.contains(position)) {
            positionsDownloaded.add(position);
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (positionsDownloaded.contains(position)) {
                return@OnClickListener;
            } else if (positionsSelected.contains(position)) {
                positionsSelected.remove(position);
                viewModel.downloadableSongsSelected.add(item);
            } else {
                positionsSelected.add(position);
                viewModel.downloadableSongsSelected.add(item);
            }

            notifyItemChanged(position);
        })

        onItemChange?.invoke(holder.itemView, position,
            positionsSelected, positionsDownloaded);
    }

    override fun getItemCount(): Int {
        return downloadableSongs.size;
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