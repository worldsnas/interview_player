package com.ayalus.exoplayer2example.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ayalus.exoplayer2example.boxoffice.BoxOfficeFragment;
import com.ayalus.exoplayer2example.other1.BlankFragment;
import com.ayalus.exoplayer2example.other2.BlankFragment2;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return BoxOfficeFragment.newInstance();
        } else if (i == 1) {
            return BlankFragment.newInstance("", "");

        } else
            return BlankFragment2.newInstance("", "");

    }

    @Override
    public int getCount() {
        return 3;
    }
}
