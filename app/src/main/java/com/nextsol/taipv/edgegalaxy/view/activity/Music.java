package com.nextsol.taipv.edgegalaxy.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.view.adapter.ViewpagerAdapter;
import com.nextsol.taipv.edgegalaxy.view.fragment.Fragments;
import com.nextsol.taipv.edgegalaxy.view.fragment.LocalMusic;
import com.nextsol.taipv.edgegalaxy.view.fragment.OnlineMusic;

import java.util.ArrayList;
import java.util.List;

public class Music extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

//        ActionBar actionBar=getSupportActionBar();
//        actionBar.hide();
        initView();
        initEvents();

    }

    private void initEvents() {
        List<Fragments> list=new ArrayList<>();
        list.add(new Fragments(LocalMusic.newInstance(),"Local Music"));
        list.add(new Fragments(OnlineMusic.newInstance(),"Online Music"));
        ViewpagerAdapter adapter=new ViewpagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        tabLayout=findViewById(R.id.tab_music);
        viewPager=findViewById(R.id.viewpager_music);
    }
}
