package com.ayalus.exoplayer2example.boxoffice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ayalus.exoplayer2example.R;
import com.ayalus.exoplayer2example.Song;
import com.ayalus.exoplayer2example.main.MainActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.song_photo)
    SimpleDraweeView photo;
    @BindView(R.id.song_title)
    AppCompatTextView title;
    @BindView(R.id.song_artist)
    AppCompatTextView artist;

    public SongViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bind(Song song){
        Bitmap bitmap = BitmapFactory.decodeFile(song.photourl);
        photo.setImageBitmap(bitmap);
        title.setText(song.mSongTitle);
        artist.setText(song.mArtistName);
    }

    @Override
    public void onClick(View v) {

    }
}
