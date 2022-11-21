package com.example.android.youtubemusicplayer

import android.media.MediaPlayer
import com.example.android.youtubemusicplayer.database.Song

class MusicPlayer {
    var mediaplayer = MediaPlayer();
    lateinit var currentSong: Song;

    private fun prepareSong(song: Song) {
        currentSong = song;
        mediaplayer = MediaPlayer();

        mediaplayer.setDataSource(currentSong.path);
        mediaplayer.prepare();
    }

    private fun startSong() {
        mediaplayer.start();
    }

    private fun stopSong() {
        mediaplayer.stop();
    }

    fun playSong(song: Song) {
        if (!::currentSong.isInitialized) {
            prepareSong(song);
            startSong();
        } else if (currentSong.equals(song)) {
            startSong();
        } else {
            stopSong();
            prepareSong(song);
            startSong();
        }
    }

    fun pauseSong() {
        mediaplayer.pause();
    }
}