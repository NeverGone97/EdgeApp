package com.nextsol.taipv.edgegalaxy.view;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.blankj.utilcode.util.CleanUtils;
import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.view.activity.PowerSavingMode;
import com.ram.speed.booster.RAMBooster;
import com.roger.catloadinglibrary.CatLoadingView;

import org.jetbrains.annotations.NotNull;

import kotlin.Pair;
import r21nomi.com.glrippleview.AnimationUtil;
import r21nomi.com.glrippleview.GLRippleView;

import static android.content.Context.AUDIO_SERVICE;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class ControlCenter extends Fragment implements View.OnClickListener {
    private SeekBar sbBrightness, sbVollumn;
    int x, y;
    private WifiManager wifiManager;
    private ImageView imgWifi, imgBlue, imgSync, img_arimode, imgRotate, imgDisturb, imgCamera, imgSetAlarm, imgFlash, imgClear,imgTouchOff;
    private BluetoothAdapter mBluetoothAdapter;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private CameraManager camManager;
    private RAMBooster ramBooster;
    private CatLoadingView catLoadingView;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkpermis();
        try {
            requestpermis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(hasWriteSettingsPermission(getContext())){
            changeWriteSettingsPermission(getContext());
        }else {
            return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.control_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        try {
            initEvent();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
//        final GLRippleView glRippleView = view.findViewById(R.id.glRippleView);
//        GLRippleView.Listener gl = new GLRippleView.Listener() {
//            @Override
//            public void onTouchEvent(@NotNull MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    glRippleView.setRipplePoint(new Pair(AnimationUtil.INSTANCE.map(event.getX(), 0f, getWidth(), -1f, 1f),
//                            AnimationUtil.INSTANCE.map(event.getY(), 0f, getHeight(), -1f, 1f)));
//                    float var2 = AnimationUtil.INSTANCE.map(event.getX() / getWidth(), 0.0F, 1.0F, 0.0F, 0.02F);
//                    glRippleView.setRippleOffset(var2);
//                }
//            }
//        };
//        glRippleView.setListener(gl);
//        glRippleView.setFadeDuration(1000);
//        glRippleView.startCrossFadeAnimation();
    }

    private void initEvent() throws Settings.SettingNotFoundException {
        imgWifi.setOnClickListener(this);
        imgBlue.setOnClickListener(this);
        imgSync.setOnClickListener(this);
        img_arimode.setOnClickListener(this);
        imgRotate.setOnClickListener(this);
        imgDisturb.setOnClickListener(this);
        imgFlash.setOnClickListener(this);
        imgCamera.setOnClickListener(this);
        imgSetAlarm.setOnClickListener(this);
        imgClear.setOnClickListener(this);
        imgTouchOff.setOnClickListener(this);
        if (wifiManager.isWifiEnabled()) {
            imgWifi.setImageResource(R.drawable.status_bar_toggle_wifi_on);
        }
        if (mBluetoothAdapter.isEnabled()) {
            imgBlue.setImageResource(R.drawable.status_bar_toggle_bluetooth_on);
        }
        if (ContentResolver.getMasterSyncAutomatically()) {
            imgSync.setImageResource(R.drawable.status_bar_toggle_sync_on);
        }
        if (android.provider.Settings.System.getInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            imgRotate.setImageResource(R.drawable.ic_unlock_rotate);
            imgRotate.setBackgroundResource(R.drawable.border_shadow);
        }
        int oldBrightness = Settings.System.getInt(getContext().getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS);
        Log.d("xxx", "initEvent: " + oldBrightness);
        sbBrightness.setProgress((int) (oldBrightness / 2.55));
        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Check whether has the write settings permission or not.
                boolean settingsCanWrite = hasWriteSettingsPermission(getContext());

                // If do not have then open the Can modify system settings panel.
                x = (int) (progress * 2.55);
                if (!settingsCanWrite) {
                    changeWriteSettingsPermission(getContext());
                } else {
                    changeScreenBrightness(getContext(), x);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress((int) (x / 2.55));
            }
        });
        final AudioManager am = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);
        final int volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("xxx", "initEvent:vollumn " + volume_level);
        sbVollumn.setProgress((int) (volume_level * 100 / 15));
        sbVollumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (progress * 15 / 100), 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initView(View view) {
        catLoadingView=new CatLoadingView();
        sbBrightness = view.findViewById(R.id.sb_brightness);
        sbVollumn = view.findViewById(R.id.sb_volumn);
        imgWifi = view.findViewById(R.id.img_wifi);
        imgBlue = view.findViewById(R.id.img_bluetooth);
        imgSync = view.findViewById(R.id.img_sync);
        imgRotate = view.findViewById(R.id.rotate_off);
        img_arimode = view.findViewById(R.id.img_arimode);
        imgDisturb = view.findViewById(R.id.disturb);
        imgFlash = view.findViewById(R.id.img_touch);
        imgSetAlarm = view.findViewById(R.id.img_setalarm);
        imgCamera = view.findViewById(R.id.img_camera);
        imgClear = view.findViewById(R.id.img_clear);
        imgTouchOff=view.findViewById(R.id.img_tourch_off);
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getActivity().checkSelfPermission(Manifest.permission.WRITE_SETTINGS);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_SETTINGS},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasWriteSettingsPermission(Context context) {
        boolean ret = true;
        // Get the result from below code.
        ret = Settings.System.canWrite(context);
        return ret;
    }

    // Start can modify system settings panel to let user change the write settings permission.
    private void changeWriteSettingsPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        context.startActivity(intent);
    }

    // This function only take effect in real physical android device,
    // it can not take effect in android emulator.
    private void changeScreenBrightness(Context context, int screenBrightnessValue) {
        // Change the screen brightness change mode to manual.
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue);

        /*
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = screenBrightnessValue / 255f;
        window.setAttributes(layoutParams);
        */
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_wifi:
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    imgWifi.setImageResource(R.drawable.status_bar_toggle_wifi_off);

                } else {

                    wifiManager.setWifiEnabled(true);
                    imgWifi.setImageResource(R.drawable.status_bar_toggle_wifi_on);

                }
                break;
            case R.id.img_bluetooth:
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                    imgBlue.setImageResource(R.drawable.status_bar_toggle_bluetooth_off);
                } else {
                    mBluetoothAdapter.enable();
                    imgBlue.setImageResource(R.drawable.status_bar_toggle_bluetooth_on);
                }
                break;
            case R.id.img_sync:
                if (ContentResolver.getMasterSyncAutomatically()) {
                    ContentResolver.setMasterSyncAutomatically(false);
                    imgSync.setImageResource(R.drawable.status_bar_toggle_sync_off);

                } else {
                    ContentResolver.setMasterSyncAutomatically(true);
                    imgSync.setImageResource(R.drawable.status_bar_toggle_sync_on);

                }
                break;

            case R.id.img_arimode:
                Intent i = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
                break;
            case R.id.rotate_off:
                if (android.provider.Settings.System.getInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                    android.provider.Settings.System.putInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                    Toast.makeText(getContext(), "Rotation OFF", Toast.LENGTH_SHORT).show();
                    imgRotate.setImageResource(R.drawable.ic_lock_rotate);
                    imgRotate.setBackgroundResource(R.drawable.rotate_bg);
                } else {
                    android.provider.Settings.System.putInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
                    Toast.makeText(getContext(), "Rotation ON", Toast.LENGTH_SHORT).show();
                    imgRotate.setImageResource(R.drawable.ic_unlock_rotate);
                    imgRotate.setBackgroundResource(R.drawable.border_shadow);
                }
                break;
            case R.id.disturb:
                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                // Check if the notification policy access has been granted for the app.
                if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    getContext().startActivity(intent);
                }
                break;
            case R.id.img_camera:
                intentCamera();
                break;
            case R.id.img_setalarm:
                intentAlarm();
                break;
            case R.id.img_touch:
                turnFlashlightOn();
                imgTouchOff.setVisibility(View.VISIBLE);
                imgFlash.setVisibility(View.GONE);
                Log.d(TAG, "onClick:plpl ");
                break;
            case R.id.img_tourch_off:
                turnFlashlightOff();
                imgTouchOff.setVisibility(View.GONE);
                imgFlash.setVisibility(View.VISIBLE);
                break;
            case R.id.img_clear:
                intentClear();
                break;
        }
    }

    private void intentClear() {
        Intent intent=new Intent(getActivity(), CleartActivity.class);
        startActivity(intent);

    }

    private void intentAlarm() {
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
        i.putExtra(AlarmClock.EXTRA_HOUR, 10);
        i.putExtra(AlarmClock.EXTRA_MINUTES, 30);
        startActivity(i);
    }

    private void intentCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getContext().startActivity(intent);
    }

    public boolean run(Context context) {
        boolean isEnabled = isAirplaneModeOn(context);
        // Toggle airplane mode.
        setSettings(context, isEnabled ? 1 : 0);
        // Post an intent to reload.
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", !isEnabled);
        context.sendBroadcast(intent);
        return true;
    }

    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public boolean checkpermis() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getContext().checkSelfPermission(Manifest.permission.SET_ALARM);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestpermis() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.SET_ALARM, Manifest.permission.CAMERA,Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR},
                    99);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void setSettings(Context context, int value) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.putInt(
                    context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, value);
        } else {
            Settings.Global.putInt(
                    context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, value);
        }
    }

    private void turnFlashlightOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                camManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                String cameraId = null; // Usually front camera is at 0 position.
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(cameraId, true);
                }
            } catch (CameraAccessException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            mCamera = Camera.open();
            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }

    private void turnFlashlightOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                String cameraId;
                camManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                    camManager.setTorchMode(cameraId, false);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            mCamera = Camera.open();
            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            mCamera.stopPreview();
        }
    }
    private int getWidth() {
        Point size = new Point();
        getDisplay().getSize(size);
        return size.x;

    }

    private int getHeight() {
        Point size = new Point();
        getDisplay().getSize(size);
        return size.y;
    }

    private Display getDisplay() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();


        return display;
    }
}
