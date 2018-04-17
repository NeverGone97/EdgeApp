package com.nextsol.taipv.edgegalaxy.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.nextsol.taipv.edgegalaxy.R;

public class PowerSavingMode extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_saving_activity);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Power saving mode");
    }
}
