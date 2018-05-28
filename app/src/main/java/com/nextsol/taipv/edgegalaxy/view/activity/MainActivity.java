package com.nextsol.taipv.edgegalaxy.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.Constants;
import com.nextsol.taipv.edgegalaxy.model.PeopleContact;
import com.nextsol.taipv.edgegalaxy.utils.SharePre;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_CODE = 2;
    private LinearLayout linearLayout, linearPermis, itemShare, item_power_saving,
            item_screen_edge, itemThemPaper, itemEdgeScreen, itemAdvance, itemIcon,
            itemPeopleEdge, itemAppEdge, itemMusic, itemRingtone;
    List<PeopleContact> contacts;
    int ccc;
    SharePre sharePre;
    private int number = 0;
    TextView tv_state;
    private TextView tvPermission, numberContact;
    private SwitchCompat switchCompat;
    ImageView banner;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_state = findViewById(R.id.tv_state);
        sharePre = new SharePre(MainActivity.this);
        getContactList();

        if (sharePre.getBoolean(Constants.checkSwich)) {
            initializeView();
            tv_state.setText("On");

        } else {
            tv_state.setText("Off");

        }
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
        itemRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                        if (Settings.System.canWrite(MainActivity.this)) {
//                            setRingtone();

                            if (checkPermission()) {
                                Intent intent = new Intent(MainActivity.this, NavigationHome.class);
                                intent.putExtra(Constants.putFrag, 3);
                                startActivity(intent);
                            } else {
                                requestPermission();
                            }

                            } else {
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                                    .setData(Uri.parse("package:" + getPackageName()))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }


                        Log.e("value", "Permission already Granted, Now you can save image.");

                } else {
//                    setRingtone();
                    Log.e("value", "Not required for requesting runtime permission");
                }

            }
        });
//        switchCompat.setOnCheckedChangeListener(this);
        Log.d("xxx", "initEvents: " + sharePre.getBoolean(Constants.checkSwich));
        switchCompat.setChecked(sharePre.getBoolean(Constants.checkSwich));

//        if(sharePre.getBoolean(Constants.checkSwich)==true){
//            initializeView();
//        }else {
//            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//        }


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharePre.putBoolean(Constants.checkSwich, isChecked);
                if (isChecked) {
                    checkPermissionForReadExtertalStorage();
                    try {
                        requestPermissionForReadExtertalStorage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {


                        //If the draw over permission is not available open the settings screen
                        //to grant the permission.
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                    } else {
                        initializeView();
                        tv_state.setText("On");
                    }

                } else {
                    tv_state.setText("Off");
                }
            }
        });
    }

    private void initView() {
        banner = findViewById(R.id.banner);
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
        itemRingtone = findViewById(R.id.item_ringtone);
        numberContact = findViewById(R.id.numberContact);
        switchCompat = findViewById(R.id.swichButton);
        if (contacts!=null) {
            numberContact.setText(String.valueOf(number) + " People");
        } else {
            numberContact.setText("0 People");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getContactList();
        numberContact.setText(String.valueOf(number) + " People");
    }

    private void getContactList() {
        String listContact = sharePre.getListContact();
        if (!listContact.equals("NullContact")) {

            Type type = new TypeToken<List<PeopleContact>>() {
            }.getType();
            contacts = new Gson().fromJson(listContact, type);
            number = 0;
            for (int i = 0; i < 12; i++) {
                if (contacts.get(i).getName() != null && !contacts.get(i).getName().equals("Add contacts")) {
                    Log.d(TAG, "getContactList: " + contacts.get(i).getName() + " : " + i);
                    number++;
                }
            }
//        Log.d(TAG, "getContactList: "+contacts.size());
            if (contacts == null) {
                contacts = new ArrayList<>();
            }
        }else {
            return;
        }
    }

    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {
        findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_appsuggest_2);
                intent(icon);
                finish();
            }
        });
    }

    private void intent(Bitmap resoureImage) {
        Intent intent = new Intent(MainActivity.this, FloatingViewService.class);
        intent.putExtra(Constants.putImage, resoureImage);
        startService(intent);
//        finish();
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
                if (Build.VERSION.SDK_INT>=23){
                    if(checkPermission()){
                        intentActivity(Music.class);
                    }else {
                        requestPermissionMusic();
                    }
                }else {
                    intentActivity(Music.class);
                }
                break;
            case R.id.item_ringtone:
                Intent intent = new Intent(MainActivity.this, NavigationHome.class);
                intent.putExtra(Constants.putFrag, 3);
                startActivity(intent);
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
        if (isChecked) {
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
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    private void requestPermissionMusic() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can save image .");
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (Settings.System.canWrite(MainActivity.this)) {
//                            setRingtone();
                            Intent intent = new Intent(MainActivity.this, NavigationHome.class);
                            intent.putExtra(Constants.putFrag, 3);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                                    .setData(Uri.parse("package:" + getPackageName()))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                } else {
                    Log.e("value", "Permission Denied, You cannot save image.");
                }
                break;
            case PERMISSION_CODE:
                intentActivity(Music.class);
                break;
        }
    }
}