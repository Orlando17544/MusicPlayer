package com.example.android.youtubemusicplayer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.youtubemusicplayer.albums.AlbumsFragment
import com.example.android.youtubemusicplayer.artists.ArtistsFragment
import com.example.android.youtubemusicplayer.genres.GenresFragment
import com.example.android.youtubemusicplayer.playlists.PlaylistFragment
import com.example.android.youtubemusicplayer.songs.SongsFragment

class CategoriesPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 5;
    }

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> {
                val fragment: SongsFragment = SongsFragment();
                return fragment;
            }
            1 -> {
                val fragment = PlaylistFragment();
                return fragment;
            }
            2 -> {
                val fragment = AlbumsFragment();
                return fragment;
            }
            3 -> {
                val fragment = ArtistsFragment();
                return fragment;
            }
            4 -> {
                val fragment = GenresFragment();
                return fragment
            }
            else -> {
                val fragment = Fragment();
                return fragment
            }
        }
    }
}