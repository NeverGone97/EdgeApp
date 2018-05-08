package com.nextsol.taipv.edgegalaxy.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nextsol.taipv.edgegalaxy.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    private LinearLayout linearLayout, linearPermis, itemShare, item_power_saving,
            item_screen_edge, itemThemPaper, itemEdgeScreen, itemAdvance, itemIcon,
            itemPeopleEdge, itemAppEdge, itemMusic, itemRingtone;
    private TextView tvPermission,numberContact;
    private SwitchCompat switchCompat;
    ImageView banner;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.

    }

    private void initEvents() {
        linearPermis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout.isShown()) {
                    AnimationExpand.slide_up(MainActivity.this, linearLayout);
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    AnimationExpand.slide_down(MainActivity.this, linearLayout);
                }
            }
        });

        banner.setOnClickListener(this);
        itemShare.setOnClickListener(this);
        item_power_saving.setOnClickListener(this);
        item_screen_edge.setOnClickListener(this);
        itemThemPaper.setOnClickListener(this);
        itemEdgeScreen.setOnClickListener(this);
        itemAdvance.setOnClickListener(this);
        itemIcon.setOnClickListener(this);
        itemPeopleEdge.setOnClickListener(this);
        itemAppEdge.setOnClickListener(this);
        itemMusic.setOnClickListener(this);
        itemRingtone.setOnClickListener(this);
//        switchCompat.setOnCheckedChangeListener(this);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {


                        //If the draw over permission is not available open the settings screen
                        //to grant the permission.
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                    } else {
                        initializeView();
                    }
                }
            }
        });
    }

    private void initView() {
        banner=findViewById(R.id.banner);
        linearLayout = findViewById(R.id.hide_permiss);
        linearLayout.setVisibility(View.GONE);
        tvPermission = findViewById(R.id.tv_permission);
        linearPermis = findViewById(R.id.lilearPermiss);
        itemShare = findViewById(R.id.item_share);
        item_power_saving = findViewById(R.id.item_power_saving);
        item_screen_edge = findViewById(R.id.item_edge_screen);
        itemThemPaper = findViewById(R.id.item_theme_paper);
        itemEdgeScreen = findViewById(R.id.item_edge);
        itemAdvance = findViewById(R.id.item_advanced);
        itemIcon = findViewById(R.id.item_icon);
        itemPeopleEdge = findViewById(R.id.item_people_edge);
        itemAppEdge = findViewById(R.id.item_edge_app);
        itemMusic = findViewById(R.id.item_music);
        itemRingtone=findViewById(R.id.item_ringtone);
        numberContact=findViewById(R.id.numberContact);
        switchCompat=findViewById(R.id.swichButton);
    }

    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {
        findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionForReadExtertalStorage();
                try {
                    requestPermissionForReadExtertalStorage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_share:
                shareTextIntent("You using Screen Edge App");
                break;
            case R.id.item_power_saving:
                intentActivity(PowerSavingMode.class);
                break;
            case R.id.item_edge_screen:
                intentActivity(EdgeScreenNotifi.class);
                break;
            case R.id.item_theme_paper:
                intentActivity(ThemesWallpaper.class);
                break;
            case R.id.item_edge:
                intentActivity(EdgeScreen.class);
                break;
            case R.id.item_advanced:
                showDialogAdvance();
                break;
            case R.id.item_icon:
                intentActivity(IconActivity.class);
                break;
            case R.id.item_people_edge:
                intentActivity(PeopleEdge.class);
                break;
            case R.id.item_edge_app:
                intentActivity(AppsEdge.class);
                break;
            case R.id.item_music:
                intentActivity(Music.class);
                break;
            case R.id.item_ringtone:
                intentActivity(NavigationHome.class);
                break;
            case R.id.banner:
                intentActivity(WeatherUtils.class);
                break;
        }
    }

    private void showDialogAdvance() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_advance, null);
        alert.setView(mView);
        AlertDialog dialog = alert.create();
        dialog.show();

    }

    private void intentActivity(Class powerSavingModeClass) {
        Intent intent = new Intent(this, powerSavingModeClass);
        startActivity(intent);
    }

    private void shareTextIntent(String s) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, s);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            intentActivity(UtilsWidget.class);
        }
    }
    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(Manifest.permission.READ_CALENDAR);
            return result == PackageManager.PERMISSION_GRANTED;


        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}