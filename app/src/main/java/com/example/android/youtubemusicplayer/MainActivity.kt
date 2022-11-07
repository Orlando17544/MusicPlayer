package com.example.android.youtubemusicplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        topAppBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.download_music -> {
                    val intent = Intent(this@MainActivity, DownloadMusicActivity::class.java)
                    startActivity(intent);
                }
            }
            false
        })

        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager2)

        viewPager2.adapter = CategoriesPagerAdapter(this)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        val tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Songs"
                1 -> tab.text = "Playlists"
                2 -> tab.text = "Albums"
                3 -> tab.text = "Artists"
                4 -> tab.text = "Genders"
            }
        }
        tabLayoutMediator.attach()
    }
}