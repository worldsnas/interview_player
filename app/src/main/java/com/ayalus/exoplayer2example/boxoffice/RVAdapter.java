package com.ayalus.exoplayer2example.boxoffice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ayalus.exoplayer2example.R;
import com.ayalus.exoplayer2example.Song;
import com.ayalus.exoplayer2example.main.MainActivity;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<SongViewHolder> {
    List<Song> songList;

    public RVAdapter(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SongViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_song, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(SongViewHolder songViewHolder, int i) {
        songViewHolder.bind(songList.get(i));
        songViewHolder.itemView.setOnClickListener(v-> {
            Context context = v.getContext();
            if (context != null && context instanceof MainActivity){
                MainActivity mainActivity = ((MainActivity) context);
                mainActivity.play(songList, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (songList != null) {
            return songList.size();
        } else
            return 0;
    }
}
