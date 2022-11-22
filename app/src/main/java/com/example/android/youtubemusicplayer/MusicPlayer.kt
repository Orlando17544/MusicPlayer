package com.example.android.youtubemusicplayer

import android.media.MediaPlayer
import com.example.android.youtubemusicplayer.database.Song

object MusicPlayer {
    private var mediaplayer = MediaPlayer();
    private lateinit var currentSong: Song;

    var paused = true;

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

    fun playSong(song: Song = currentSong) {
        if (!::currentSong.isInitialized) {
            prepareSong(song);
        } else if (!currentSong.equals(song)) {
            stopSong();
            prepareSong(song);
        }
        startSong();
        paused = false;
    }

    fun pauseSong() {
        mediaplayer.pause();
        paused = true;
    }
}