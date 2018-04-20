package com.nextsol.taipv.edgegalaxy.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.view.adapter.AppsEdgeAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppsEdge extends AppCompatActivity {
    private static final String TAG = "xxx";
    AppsEdgeAdapter adapter;
    List<com.nextsol.taipv.edgegalaxy.model.AppsEdge> list;
    int poss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_edge);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.rcv_edge_apps);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.plus_btn_selected);
        for(int i=0;i<15;i++){
            list.add(new com.nextsol.taipv.edgegalaxy.model.AppsEdge("",icon));
        }
        adapter = new AppsEdgeAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            byte[] b = extras.getByteArray("icon");
            final String a = extras.getString("name");
            final int c=extras.getInt("poss",-1);
            final Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
            Log.d("MyAdapter", "onActivityResult" + bmp.toString() + a);

//
//            list.set(9,new com.nextsol.taipv.edgegalaxy.model.AppsEdge("Hehe",bmp));
            adapter.upDateIem(c,new com.nextsol.taipv.edgegalaxy.model.AppsEdge(a,bmp,R.drawable.weather_delete_btn));

//            adapter.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onItemClick(int position, Object object) {
//                adapter.addItem(0,new com.nextsol.taipv.edgegalaxy.model.AppsEdge(a,bmp));
//                Log.d(TAG, "onItemClick: nhu cut");
//            }
        }

//        adapter.notifyDataSetChanged();
        }

//    @Override
//    public void iPassPoss(int pos) {
//        poss=pos;
//    }
}