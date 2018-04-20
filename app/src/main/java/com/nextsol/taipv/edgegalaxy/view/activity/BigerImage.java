package com.nextsol.taipv.edgegalaxy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.ImageGrid;
import com.nextsol.taipv.edgegalaxy.view.adapter.GridWallBigerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BigerImage extends AppCompatActivity {
ImageView imageView;
RecyclerView recyclerView;
List<ImageGrid>list;
    private  Integer b[]={R.drawable.gift_background,R.drawable.gift_background2,R.drawable.gift_background2,
            R.drawable.edge_wallpaper_s8,R.drawable.baymax};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biger_image);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Gellary");
        Intent intent=getIntent();
        int icon=intent.getIntExtra("icon",-1);
    imageView=findViewById(R.id.img_biger);
    imageView.setImageResource(icon);
    recyclerView=findViewById(R.id.grid_biger);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        list=new ArrayList<>();
        for(int i=0;i<b.length;i++){
            list.add(new ImageGrid(b[i]));
        }
        GridWallBigerAdapter adapter=new GridWallBigerAdapter(list,this);
        recyclerView.setAdapter(adapter);
    }
}
