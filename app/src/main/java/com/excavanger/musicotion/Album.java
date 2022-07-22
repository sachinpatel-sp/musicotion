package com.excavanger.musicotion;

import static com.excavanger.musicotion.Song.EMPTY_SONG;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Album {

    public final List<Song> songs;

    public Album() {
        this.songs = new ArrayList<>();
    }

    public String getTitle() {
        return getFirstSong().albumName;
    }

    public int getArtistId() {
        return getFirstSong().artistId;
    }

    public String getArtistName() {
        return getFirstSong().artistName;
    }

    public int getYear() {
        return getFirstSong().year;
    }

    public int getSongCount() {
        return songs.size();
    }

    @NonNull
    private Song getFirstSong() {
        return songs.isEmpty() ? EMPTY_SONG : songs.get(0);
    }
}
