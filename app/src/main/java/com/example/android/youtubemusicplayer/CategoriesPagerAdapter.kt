package com.example.android.youtubemusicplayer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CategoriesPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 5;
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = SongsFragment()
            1 -> fragment = PlaylistFragment()
            2 -> fragment = AlbumsFragment()
            3 -> fragment = ArtistsFragment()
            4 -> fragment = GendersFragment()
        }

        return fragment!!
    }
}