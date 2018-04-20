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
import com.nextsol.taipv.edgegalaxy.model.ImageGrid;
import com.nextsol.taipv.edgegalaxy.view.adapter.GridWallAdapter;

import java.util.ArrayList;
import java.util.List;

public class ItemGridView extends Fragment {
    List<ImageGrid>list;
    RecyclerView recyclerView;
    private  Integer b[]={R.drawable.gift_background,R.drawable.gift_background2,R.drawable.gift_background2,
            R.drawable.edge_wallpaper_s8,R.drawable.baymax};
    public static ItemGridView newInstance(String title) {

        Bundle args = new Bundle();

        ItemGridView fragment = new ItemGridView();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.grid_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        list=new ArrayList<>();
        for(int i=0;i<b.length;i++){
            list.add(new ImageGrid(b[i]));
        }
        GridWallAdapter adapter=new GridWallAdapter(list,getContext());
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        recyclerView=view.findViewById(R.id.rcv_item_grid);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
