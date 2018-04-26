package com.nextsol.taipv.edgegalaxy.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.nextsol.taipv.edgegalaxy.R;

import static android.content.Context.AUDIO_SERVICE;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class ControlCenter extends Fragment {
    private SeekBar sbBrightness, sbVollumn;
    int x,y;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.control_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        checkPermissionForReadExtertalStorage();
//        try {
//            requestPermissionForReadExtertalStorage();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        initView(view);
        try {
            initEvent();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initEvent() throws Settings.SettingNotFoundException {

        int oldBrightness = Settings.System.getInt(getContext().getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS);
        Log.d("xxx", "initEvent: "+oldBrightness);
        sbBrightness.setProgress((int)(oldBrightness/2.55));
        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Context context = getContext();

                // Check whether has the write settings permission or not.
                boolean settingsCanWrite = hasWriteSettingsPermission(context);
                // If do not have then open the Can modify system settings panel.
                 x = (int) (progress * 2.55);
                if (!settingsCanWrite) {
                    changeWriteSettingsPermission(context);
                } else {
                    changeScreenBrightness(context, x);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress((int)(x/2.55));
            }
        });
        final AudioManager am = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);
        final int volume_level= am.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("xxx", "initEvent:vollumn "+volume_level);
        sbVollumn.setProgress((int)(volume_level*100/15));
        sbVollumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(progress*15/100),0);
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
        sbBrightness = view.findViewById(R.id.sb_brightness);
        sbVollumn = view.findViewById(R.id.sb_volumn);
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

}
