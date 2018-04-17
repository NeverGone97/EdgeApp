package com.nextsol.taipv.edgegalaxy.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.PeopleContact;
import com.nextsol.taipv.edgegalaxy.view.adapter.PeopleAdapter;

import java.util.ArrayList;
import java.util.List;

public class PeopleEdge extends AppCompatActivity {
    RecyclerView recyclerView;
    Integer[] listColor = {R.drawable.circle_cornor_oran, R.drawable.circle_cornor_yellow, R.drawable.circle_cornor_green,
            R.drawable.circle_cornor_blue2, R.drawable.circle_cornor_blue, R.drawable.circle_cornor_red,
            R.drawable.circle_cornor_oran, R.drawable.circle_cornor_yellow, R.drawable.circle_cornor_green,
            R.drawable.circle_cornor_blue2, R.drawable.circle_cornor_blue, R.drawable.circle_cornor_red};
    List<PeopleContact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_edge);
        initView();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.rcv_people_edge);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        contacts = new ArrayList<>();
        for (int i = 0; i < listColor.length; i++) {
            contacts.add(new PeopleContact(listColor[i]));
        }
        PeopleAdapter adapter = new PeopleAdapter(this, contacts);
        recyclerView.setAdapter(adapter);
    }
}
