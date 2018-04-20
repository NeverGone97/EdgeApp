package com.nextsol.taipv.edgegalaxy.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.IconIcon;
import com.nextsol.taipv.edgegalaxy.view.adapter.IconItem;

import java.util.ArrayList;
import java.util.List;

public class Icon extends Fragment {
    List<IconIcon> list;
    RecyclerView recyclerView;
    Integer aaa[] = {R.drawable.ic_launcher_viber, R.drawable.ic_app_wall_carrot3, R.drawable.ic_app_wall_carrot1
            , R.drawable.gift_background2, R.drawable.ic_launcher_telegram, R.drawable.icon_appsuggest_2, R.drawable.icon_coin
            , R.drawable.weather_sunny, R.drawable.icon_questionmark, R.drawable.ic_favorite, R.drawable.ic_launcher_viber, R.drawable.ic_app_wall_carrot3, R.drawable.ic_app_wall_carrot1
            , R.drawable.gift_background2, R.drawable.ic_launcher_telegram, R.drawable.icon_appsuggest_2, R.drawable.icon_coin
            , R.drawable.weather_sunny, R.drawable.icon_questionmark, R.drawable.ic_favorite};

    public static Icon newInstance(String title) {
        Bundle args = new Bundle();

        Icon fragment = new Icon();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_icon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        list=new ArrayList<>();
        for(int i=0;i<aaa.length;i++){
            list.add(new IconIcon(aaa[i]));
        }
        IconItem adapter=new IconItem(list,getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rcv_icon);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
