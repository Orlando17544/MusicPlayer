package com.example.android.youtubemusicplayer

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.android.youtubemusicplayer.download_music.DownloadMusicActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        topAppBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.download_music -> {
                    val intent = Intent(this@MainActivity, DownloadMusicActivity::class.java)
                    launcher.launch(intent);
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

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
        if (result.resultCode == RESULT_OK) {
            val songsToDownload : Array<Parcelable> =
                result.data?.extras?.getParcelableArray("songsToDownload") as Array<Parcelable>;

            viewModel.downloadMusicFiles(songsToDownload);
        }
    })


}