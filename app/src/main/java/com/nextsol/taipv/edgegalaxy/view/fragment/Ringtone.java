package com.nextsol.taipv.edgegalaxy.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.view.adapter.ViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Ringtone extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    List<Fragments> listFragments;
    String a[]={"Galaxy","Iphone"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ringtone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {

    }

    private void initView(View view) {
        viewPager=view.findViewById(R.id.view_pager);
        tabLayout=view.findViewById(R.id.tabLayout);
        listFragments=new ArrayList<>();
        for(int i=0;i<a.length;i++){
            listFragments.add(new Fragments(new ItemGridRingView().newInstance(i),a[i]));
        }
        ViewpagerAdapter adapter=new ViewpagerAdapter(getChildFragmentManager(),listFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
