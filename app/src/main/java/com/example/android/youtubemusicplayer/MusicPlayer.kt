package com.example.android.youtubemusicplayer

import android.media.MediaPlayer
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist

object MusicPlayer {
    var mediaplayer = MediaPlayer();
    var currentSongWithAlbumAndArtist: SongWithAlbumAndArtist? = null;

    var paused = true;

    private fun prepareSong(songWithAlbumAndArtist: SongWithAlbumAndArtist) {
        currentSongWithAlbumAndArtist = songWithAlbumAndArtist;
        mediaplayer = MediaPlayer();

        mediaplayer.setDataSource(currentSongWithAlbumAndArtist?.song?.path);
        mediaplayer.prepare();
    }

    private fun startSong() {
        mediaplayer.start();
    }

    private fun stopSong() {
        mediaplayer.stop();
    }

    fun playSong(songWithAlbumAndArtist: SongWithAlbumAndArtist? = currentSongWithAlbumAndArtist) {
        if (currentSongWithAlbumAndArtist == null) {
            songWithAlbumAndArtist?.let { prepareSong(it) };
        } else if (!currentSongWithAlbumAndArtist!!.equals(songWithAlbumAndArtist)) {
            stopSong();
            songWithAlbumAndArtist?.let { prepareSong(it) };
        }
        startSong();
        paused = false;
    }

    fun pauseSong() {
        mediaplayer.pause();
        paused = true;
    }
}