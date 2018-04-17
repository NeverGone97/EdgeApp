package com.nextsol.taipv.edgegalaxy.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.AppList;
import com.nextsol.taipv.edgegalaxy.view.adapter.AppAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShowListApp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_app_installed);
        String name;
        Drawable icon;
        final ListView userInstalledApps = findViewById(R.id.installed_app_list);

        final List<AppList> installedApps = getInstalledApps();
        AppAdapter installedAppAdapter = new AppAdapter(this, installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);
        userInstalledApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap bitmap = ((BitmapDrawable)installedApps.get(position).getIcon()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                Intent intent=getIntent();
                int a=intent.getIntExtra("pos",-1);
                Toast.makeText(ShowListApp.this, ""+a, Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                        returnIntent.putExtra("icon", b);
                        returnIntent.putExtra("poss",a);
                returnIntent.putExtra("name",installedApps.get(position).getName());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private List<AppList> getInstalledApps() {
        List<AppList> res = new ArrayList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                res.add(new AppList(appName, icon));
            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
}