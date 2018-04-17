package com.nextsol.taipv.edgegalaxy.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.view.adapter.EdgeScreenApdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeScreen extends AppCompatActivity {
    Integer[]mImage={R.drawable.music_edge_ic_next,R.drawable.ic_app_wall_carrot3,R.drawable.ic_launcher_viber,R.drawable.skype_blue};

    public static final String TAG="edgescreen";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edge_screen_activity);
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView=findViewById(R.id.rcv_edge_sceen);
        recyclerView.setLayoutManager(layoutManager);
        List<Integer>list=new ArrayList<>(Arrays.asList(mImage));
        EdgeScreenApdapter screenApdapter=new EdgeScreenApdapter(list,getApplicationContext());
        recyclerView.setAdapter(screenApdapter);
        recyclerView.setHasFixedSize(true);
    }
}
