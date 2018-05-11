package com.nextsol.taipv.edgegalaxy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.Constants;

public class ThemesWallpaper extends AppCompatActivity {
    Button btnThemeOn;
    SwitchCompat scWater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_wallpaper_activity);
        initView();
        initEvent();
    }

    private void initEvent() {
        btnThemeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ThemesWallpaper.this,NavigationHome.class);
                    intent.putExtra(Constants.putFrag,1);
                        startActivity(intent);
            }
        });
    }

    private void initView() {
        btnThemeOn=findViewById(R.id.btn_themes_online);
        scWater=findViewById(R.id.sc_water);
        scWater.setChecked(true);
    }
}
