package com.nextsol.taipv.edgegalaxy.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextsol.taipv.edgegalaxy.R;

import java.util.Calendar;

public class UtilsSPlaner extends Fragment {
    RecyclerView rcvListNote;
    ImageView imgAddNote;
    private TextView tvToday;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splaner_utils,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();

    }

    private void initEvent() {
        imgAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToCent();
            }
        });
    }

    private void initView(View view) {
        imgAddNote=view.findViewById(R.id.img_addNote);
        tvToday=view.findViewById(R.id.tv_today);
        rcvListNote=view.findViewById(R.id.listNote);
    }

    private void intentToCent() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);
    }
}
