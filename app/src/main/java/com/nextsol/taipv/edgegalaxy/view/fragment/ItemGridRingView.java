package com.nextsol.taipv.edgegalaxy.view.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.IPassPos;
import com.nextsol.taipv.edgegalaxy.model.Ring;
import com.nextsol.taipv.edgegalaxy.view.adapter.GridRingAdapter;

import java.util.ArrayList;
import java.util.List;

public class ItemGridRingView extends Fragment {
    GridLayoutManager gridLayoutManager;
    IPassPos iPassPos;
    MediaPlayer mediaPlayer;

    public static ItemGridRingView newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt("posring", pos);
        ItemGridRingView fragment = new ItemGridRingView();
        fragment.setArguments(args);
        return fragment;
    }

    List<Ring> list;
    RecyclerView recyclerView;
    private Integer b[] = {R.drawable.gift_background, R.drawable.gift_background2, R.drawable.gift_background2,
            R.drawable.edge_wallpaper_s8, R.drawable.baymax};
    private Integer c[] = {R.raw.galaxy, R.raw.galaxys, R.raw.iphone8, R.raw.iphonex, R.raw.iphonee};
    private String a[] = {"Galaxy S8", "Galaxy Note 8", "Galaxy S9", "Iphone 8Plus", "Iphone X"};

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
        list = new ArrayList<>();
        Log.d("posring", "initEvent: " + getArguments().getInt("posring"));
        if (getArguments().getInt("posring") == 0) {
            for (int i = 0; i < 3; i++) {
                list.add(new Ring(b[i], c[i], a[i],false));
            }
            GridRingAdapter adapter = new GridRingAdapter(list, getContext());
            recyclerView.setAdapter(adapter);
//            adapter.setOnClick(new IPassPos() {
//                @Override
//                    public void iPassPoss(int position) {
//                        if(mediaPlayer!=null){
//                            mediaPlayer.stop();
//                            mediaPlayer.release();
//                        }
//
//                     mediaPlayer = MediaPlayer.create(getContext(), list.get(position).getRing());
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.pause();
//                    } else {
//                        mediaPlayer.start();
//                    }
//                    Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
//                }
//            });
        } else {
            list.add(new Ring(b[3], c[3], a[3],false));
            list.add(new Ring(b[3], c[3], a[4],false));

            GridRingAdapter adapter = new GridRingAdapter(list, getContext());
            recyclerView.setAdapter(adapter);
        }

    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rcv_item_grid);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
