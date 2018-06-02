package com.ayalus.exoplayer2example.boxoffice;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayalus.exoplayer2example.R;
import com.ayalus.exoplayer2example.Song;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BoxOfficeFragment extends Fragment {

    List<Song> mSongs;
    @BindView(R.id.rv_songs)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    public static BoxOfficeFragment newInstance() {
        return new BoxOfficeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boxoffice, container, false);
        unbinder = ButterKnife.bind(this, view);
        getSongs();

        recyclerView.setAdapter(new RVAdapter(mSongs));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getSongs() {
        mSongs = new LinkedList<>();
        final Cursor mCursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM_ID}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");
        if (mCursor != null) {
            Random random = new Random() ;
            if (mCursor.moveToFirst()) {
                do {
                    String songname = mCursor
                            .getString(mCursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String sonpath = mCursor.getString(mCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    String artistname = mCursor.getString(mCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String albumid = mCursor
                            .getString(mCursor
                                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
//                    long id = mCursor
//                            .getLong(mCursor
//                                    .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    Uri uri = ContentUris.withAppendedId(sArtworkUri,
                            Long.valueOf(albumid));

                    mSongs.add(new Song(random.nextLong(), songname, sonpath, artistname, albumid));
                } while (mCursor.moveToNext());

            }

            Cursor cursorAudio = getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (cursorAudio != null && cursorAudio.moveToFirst()) {

                Cursor cursorAlbum = null;
                if (cursorAudio != null && cursorAudio.moveToFirst()) {

                    do {
                        Long albumId = Long.valueOf(cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                        cursorAlbum = getContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                                MediaStore.Audio.Albums._ID + "=" + albumId, null, null);

                        if(cursorAlbum != null && cursorAlbum.moveToFirst()){
                            String albumCoverPath = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                            long albumCoverID = cursorAlbum.getLong(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums._ID));
                            String data = cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.DATA));

                            for (int i = 0; i < mSongs.size(); i++) {
                                String id = mSongs.get(i).photourl;
                                if (id != null && id.equalsIgnoreCase(""+albumCoverID)){
                                    mSongs.get(i).photourl = albumCoverPath;
                                }
                            }

                        }

                    } while (cursorAudio.moveToNext());
                }
                if (cursorAlbum != null) {
                    cursorAlbum.close();
                }
            }
            if (cursorAudio != null) {
                cursorAudio.close();
            }

            mCursor.close();
        }
    }
}

