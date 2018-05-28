package com.nextsol.taipv.edgegalaxy.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.view.adapter.ViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class Ringtone extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    List<Fragments> listFragments;

    private static final int PERMISSION_REQUEST_CODE = 1;
    String a[]={"Galaxy","Iphone"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ringtone, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        Context context = getActivity().getApplicationContext();
//
//        // Check whether has the write settings permission or not.
//
//        boolean settingsCanWrite = Settings.System.canWrite(context);
//
//        if(!settingsCanWrite) {
//            // If do not have write settings permission then open the Can modify system settings panel.
//            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//            startActivity(intent);
//        }else {
//            // If has permission then show an alert dialog with message.
//            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//            alertDialog.setMessage("You have system write settings permission now.");
//            alertDialog.show();
//        }


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                if (Settings.System.canWrite(getActivity())) {
//                    setRingtone();
                } else {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                            .setData(Uri.parse("package:" + context.getPackageName()))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }


                Log.e("value", "Permission already Granted, Now you can save image.");
            } else {
                requestPermission();
            }
        } else {
//            setRingtone();

            Log.e("value", "Not required for requesting runtime permission");
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {

    }

    private boolean checkPermission() {
        int result = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can save image .");
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (Settings.System.canWrite(getActivity())) {
                        } else {
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                                    .setData(Uri.parse("package:" + getActivity().getPackageName()))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                } else {
                    Log.e("value", "Permission Denied, You cannot save image.");
                }
                break;
        }
    }
    private void initView(View view) {
        viewPager=view.findViewById(R.id.view_pager);
        tabLayout=view.findViewById(R.id.tabLayout);
        listFragments=new ArrayList<>();
        for(int i=0;i<a.length;i++){
            listFragments.add(new Fragments(new ItemGridRingView().newInstance(i),a[i]));
        }
        ViewpagerAdapter adapter=new ViewpagerAdapter(getChildFragmentManager(),listFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
