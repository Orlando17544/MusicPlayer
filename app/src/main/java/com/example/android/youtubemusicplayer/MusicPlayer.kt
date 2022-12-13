package com.example.android.youtubemusicplayer

import android.media.MediaPlayer
import com.example.android.youtubemusicplayer.database.Song
import com.example.android.youtubemusicplayer.database.SongWithAlbumAndArtist

object MusicPlayer {
    var mediaplayer = MediaPlayer();
    var currentSongWithAlbumAndArtist: SongWithAlbumAndArtist? = null;

    fun isPaused(): Boolean {
        if (isPlaying() || isCompleted()) {
            return false;
        } else {
            return true;
        }
    }

    fun isCompleted(): Boolean {
        if (mediaplayer.currentPosition.equals(mediaplayer.duration)) {
            return true;
        } else {
            return false;
        }
    }

    fun isPlaying(): Boolean {
        if (mediaplayer.isPlaying) {
            return true;
        } else {
            return false;
        }
    }

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
    }

    fun pauseSong() {
        mediaplayer.pause();
    }
}