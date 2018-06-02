package com.ayalus.exoplayer2example.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ayalus.exoplayer2example.R;
import com.ayalus.exoplayer2example.Song;
import com.ayalus.exoplayer2example.gramaphone.GramaphoneFragment;
import com.ayalus.exoplayer2example.player.PlayerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation ahBottomNavigation;
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.fragment_gramaphon_container)
    FrameLayout frameLayoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_gramaphon_container, GramaphoneFragment.newInstance(), "gramaphone").commit();

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);

        ahBottomNavigation.addItem(new AHBottomNavigationItem("روبورس", R.drawable.ic_stars_black_24dp));
        ahBottomNavigation.addItem(new AHBottomNavigationItem("صفحه گردون", R.drawable.ic_surround_sound_black_24dp));
        ahBottomNavigation.addItem(new AHBottomNavigationItem("روبورس", R.drawable.ic_surround_sound_black_24dp));
        ahBottomNavigation.setCurrentItem(0);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        ahBottomNavigation.setForceTint(true);

        ahBottomNavigation.setOnTabSelectedListener((i, selected) -> {
            if (!selected) {
                viewPager.setCurrentItem(i);
            }
            return true;
        });

    }

    public void play(List<Song> songs, int position){
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
