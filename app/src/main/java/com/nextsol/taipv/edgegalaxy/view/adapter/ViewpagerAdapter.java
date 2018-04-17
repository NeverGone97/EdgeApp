package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nextsol.taipv.edgegalaxy.view.fragment.Fragments;

import java.util.List;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {
    List<Fragments>list;

    public ViewpagerAdapter(FragmentManager fm, List<Fragments> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (list.get(position).getTitle()==null){
            return super.getPageTitle(position);
        }else {
            return list.get(position).getTitle();
        }
    }
}
