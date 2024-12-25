package com.samansepahvand.photeditorapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.samansepahvand.photeditorapp.ui.fragment.HomeFragment;
import com.samansepahvand.photeditorapp.ui.fragment.ResultFragment;

public class MyPagerAdapter  extends FragmentPagerAdapter {

    public static final int Num_FRAGMENT_COUN=2;


    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return HomeFragment.newInstance("","");
            case 1:
                return ResultFragment.newInstance("","");
        }

        return null;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page"+position;
    }

    @Override
    public int getCount() {
        return Num_FRAGMENT_COUN;
    }
}
