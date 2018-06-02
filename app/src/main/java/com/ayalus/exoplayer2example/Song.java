package com.ayalus.exoplayer2example;

public class Song {
    public final long mSongID;
    public final String mSongTitle;
    public final String mSongPath;
    public final String mArtistName;
    public String photourl;

    public Song(long mSongID, String mSongTitle, String mSongPath, String mArtistName, String photourl) {
        this.mSongID = mSongID;
        this.mSongTitle = mSongTitle;
        this.mSongPath = mSongPath;
        this.mArtistName = mArtistName;
        this.photourl = photourl;
    }

    public long getSongID() {
        return mSongID;
    }

    public String getSongTitle() {
        return mSongTitle;
    }
}
