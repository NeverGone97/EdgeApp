package com.nextsol.taipv.edgegalaxy.view.activity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.Constants;
import com.nextsol.taipv.edgegalaxy.utils.SharePre;
import com.nextsol.taipv.edgegalaxy.view.ControlCenter;
import com.nextsol.taipv.edgegalaxy.view.UtilsQuickTools;
import com.nextsol.taipv.edgegalaxy.view.UtilsSPlaner;
import com.nextsol.taipv.edgegalaxy.view.adapter.ViewpagerAdapter;
import com.nextsol.taipv.edgegalaxy.view.fragment.Fragments;
import com.nextsol.taipv.edgegalaxy.view.fragment.LocalMusic;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;


public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private SharedPreferences.Editor editor;
     ImageView img;
    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getParcelableExtra(Constants.putImage)!=null){
            Bitmap image=intent.getParcelableExtra(Constants.putImage);
            img.setImageBitmap(image);
            Log.d("xxx", "onStartCommand: "+image.toString());

        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);
        img=mFloatingView.findViewById(R.id.collapsed_iv);
//        //Set the close button
//        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
//        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //close the service and remove the from from the window
//                stopSelf();
//            }
//        });


        //Drag and move floating view using user's touch action.
//        String enImage=prefs.getString(Constants.encode,"null");
//        Log.d("encode", "onCreate: "+enImage);
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
//                                expandedView.setVisibility(View.VISIBLE);
                                Intent intent=new Intent(FloatingViewService.this,UtilsWidget.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
//                                stopSelf();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                    case MotionEvent.BUTTON_BACK:
                        if (isViewCollapsed()) {
                            collapsedView.setVisibility(View.VISIBLE);
                            expandedView.setVisibility(View.GONE);
                            Log.d("xxx", "backbutton: ");
                        }
                        return true;
                }
                return false;
            }
        });
    }


    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }
}