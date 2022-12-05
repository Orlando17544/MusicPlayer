package com.example.android.youtubemusicplayer.genders

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
import com.example.android.youtubemusicplayer.database.GenderWithSongs
import com.example.android.youtubemusicplayer.database.Gender

class GendersAdapter: ListAdapter<GenderWithSongs, GendersAdapter.GendersViewHolder>(
    GendersDiffCallback()
) {
    lateinit var onOptionsSelected: ((view: View, menuRes: Int, gender: Gender) -> Unit);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GendersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.gender_item, parent, false) as LinearLayout

        return GendersViewHolder(view);
    }

    override fun onBindViewHolder(holder: GendersViewHolder, position: Int) {
        val item = getItem(position);

        holder.itemView.findViewById<TextView>(R.id.gender_name).text = item?.gender?.name;
        holder.itemView.findViewById<TextView>(R.id.number_songs_gender).text = item?.songs?.size.toString();

        if (item.songs.size == 1) {
            holder.itemView.findViewById<TextView>(R.id.number_songs_gender).text = item.songs.size.toString() + " song";
        } else {
            holder.itemView.findViewById<TextView>(R.id.number_songs_gender).text = item.songs.size.toString() + " songs";
        }

        if (item?.gender?.name?.length ?: 0 > 20) {
            holder.itemView.findViewById<TextView>(R.id.gender_name).text = item?.gender?.name?.substring(0, 20) + "...";
        } else {
            holder.itemView.findViewById<TextView>(R.id.gender_name).text = item?.gender?.name;
        }

        holder.itemView.findViewById<LinearLayout>(R.id.clickable_gender)
            .setOnClickListener(View.OnClickListener {
                print("algo")
            })

        holder.itemView.findViewById<ImageView>(R.id.gender_options)
            .setOnClickListener(View.OnClickListener {
                onOptionsSelected.invoke(it, R.menu.menu_gender, item.gender)
            })
    }

    class GendersViewHolder(linearLayout: LinearLayout): RecyclerView.ViewHolder(linearLayout) {
        var genderName: TextView;
        var numberSongsGender: TextView;

        init {
            genderName = linearLayout.findViewById(R.id.gender_name);
            numberSongsGender = linearLayout.findViewById(R.id.number_songs_gender);
        }
    }
}

class GendersDiffCallback : DiffUtil.ItemCallback<GenderWithSongs>() {
    override fun areItemsTheSame(oldItem: GenderWithSongs, newItem: GenderWithSongs): Boolean {
        if (oldItem.gender.genderId == newItem.gender.genderId
            && oldItem.songs.size == newItem.songs.size) {
            return true;
        }
        return false;
    }

    override fun areContentsTheSame(
        oldItem: GenderWithSongs,
        newItem: GenderWithSongs
    ): Boolean {
        return oldItem == newItem;
    }
}