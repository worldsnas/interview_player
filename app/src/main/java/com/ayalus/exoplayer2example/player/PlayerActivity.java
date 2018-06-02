package com.ayalus.exoplayer2example.player;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ayalus.exoplayer2example.R;
import com.ayalus.exoplayer2example.Song;
import com.example.jean.jcplayer.JcAudio;
import com.example.jean.jcplayer.JcPlayerView;
import com.example.jean.jcplayer.JcStatus;
import com.example.jean.jcplayer.Origin;
import com.facebook.drawee.view.SimpleDraweeView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.triggertrap.seekarc.SeekArc;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity extends AppCompatActivity {

    @BindView(R.id.close)
    AppCompatImageView close;
    @BindView(R.id.gramaphone)
    AppCompatTextView gramaphone;
    @BindView(R.id.info_layout)
    RelativeLayout infoLayout;
    @BindView(R.id.backGroundBlur)
    SimpleDraweeView backGroundBlur;
    @BindView(R.id.backcircle)
    CircleImageView backCircle;
    @BindView(R.id.forward)
    AppCompatImageButton forward;
    @BindView(R.id.backward)
    AppCompatImageButton backward;
    @BindView(R.id.seek_bar)
    SeekArc seekBar;
    @BindView(R.id.tickerView)
    TickerView tickerView;


    List<Song> mSongs;
    JcPlayerView jcplayerView;
    int max;
    int position;
    @BindView(R.id.action_play)
    FloatingActionButton actionPlay;
    @BindView(R.id.song_title)
    AppCompatTextView songTitle;
    @BindView(R.id.song_artist)
    AppCompatTextView songArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        getSongs();
        tickerView.setCharacterLists(TickerUtils.provideNumberList());
        position = getIntent().getExtras().getInt("position");
        jcplayerView = new JcPlayerView(this);
        playCurrent();
        jcplayerView.registerStatusListener(new JcPlayerView.JcPlayerViewStatusListener() {
            @Override
            public void onPausedStatus(JcStatus jcStatus) {

            }

            @Override
            public void onContinueAudioStatus(JcStatus jcStatus) {
            }

            @Override
            public void onPlayingStatus(JcStatus jcStatus) {
            }

            @Override
            public void onTimeChangedStatus(JcStatus jcStatus) {
                runOnUiThread(() -> {
                    seekBar.setProgress((int) (jcStatus.getCurrentPosition() * 100 / max));
                });

            }

            @Override
            public void onCompletedAudioStatus(JcStatus jcStatus) {

            }

            @Override
            public void onPreparedAudioStatus(JcStatus jcStatus) {
                max = (int) jcStatus.getDuration();
            }
        });
        jcplayerView.registerServiceListener(new JcPlayerView.JcPlayerViewServiceListener() {
            @Override
            public void onPreparedAudio(String audioName, int duration) {

            }

            @Override
            public void onCompletedAudio() {
                playNext();
            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onContinueAudio() {

            }

            @Override
            public void onPlaying() {

            }

            @Override
            public void onTimeChanged(long currentTime) {
                runOnUiThread(() -> {
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime );
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime );
                    tickerView.setText(minutes+":"+seconds);
                    seekBar.setProgress((int) (currentTime * 100 / max));
                });

            }

            @Override
            public void updateTitle(String title) {

            }
        });


        seekBar.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                if (b)
                    jcplayerView.onProgressChanged(null, (i * max / 100), true);

            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });
        forward.setOnClickListener(v -> {
            playNext();
        });
        backward.setOnClickListener(v -> {
            if (position == 0) {
                Toast.makeText(this, "it is the first", Toast.LENGTH_SHORT).show();
            } else {
                position--;
                playCurrent();
                seekBar.setProgress(0);
            }
        });

    }

    private void playCurrent() {
        String title = mSongs.get(position).mSongTitle;
        String path = mSongs.get(position).mSongPath;
        songTitle.setText(mSongs.get(position).mSongTitle);
        songArtist.setText(mSongs.get(position).mArtistName);
        Bitmap bitmap = BitmapFactory.decodeFile(mSongs.get(position).photourl);
        backGroundBlur.setImageBitmap(bitmap);
        backCircle.setImageBitmap(bitmap);
        jcplayerView.playAudio(new JcAudio(title, path, Origin.FILE_PATH));
//        jcplayerView.createNotification();
    }


    @OnClick(R.id.close)
    public void onClose() {
        onBackPressed();
    }

    public void getSongs() {
        mSongs = new LinkedList<>();
        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM_ID}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");
        if (mCursor != null) {
            Random random = new Random();
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

            Cursor cursorAudio = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (cursorAudio != null && cursorAudio.moveToFirst()) {

                Cursor cursorAlbum = null;
                if (cursorAudio != null && cursorAudio.moveToFirst()) {

                    do {
                        Long albumId = Long.valueOf(cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                        cursorAlbum = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                                MediaStore.Audio.Albums._ID + "=" + albumId, null, null);

                        if (cursorAlbum != null && cursorAlbum.moveToFirst()) {
                            String albumCoverPath = cursorAlbum.getString(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                            long albumCoverID = cursorAlbum.getLong(cursorAlbum.getColumnIndex(MediaStore.Audio.Albums._ID));
                            String data = cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.DATA));

                            for (int i = 0; i < mSongs.size(); i++) {
                                String id = mSongs.get(i).photourl;
                                if (id != null && id.equalsIgnoreCase("" + albumCoverID)) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (jcplayerView != null) {
            jcplayerView.kill();
        }
    }

    @OnClick(R.id.action_play)
    public void onPlayClicked() {
        if (jcplayerView.isPlaying()) {
            jcplayerView.pause();
            actionPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {
            jcplayerView.continueAudio();
            actionPlay.setImageResource(R.drawable.ic_pause_black_24dp);
        }
    }

    public void playNext() {
        int size = mSongs.size();

        if (position == size - 1) {
            Toast.makeText(this, "it is the last", Toast.LENGTH_SHORT).show();
        } else {
            position++;
            playCurrent();
            seekBar.setProgress(0);
        }
    }
}
