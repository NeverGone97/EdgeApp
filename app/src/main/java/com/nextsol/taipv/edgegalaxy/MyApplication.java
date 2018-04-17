package com.nextsol.taipv.edgegalaxy;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

public class MyApplication extends Application{
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        Utils.init(context);
    }
}
