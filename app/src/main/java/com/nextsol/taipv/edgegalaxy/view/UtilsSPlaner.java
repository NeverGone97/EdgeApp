package com.nextsol.taipv.edgegalaxy.view;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.SPlaner;
import com.nextsol.taipv.edgegalaxy.view.adapter.Splaner;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.Pair;
import r21nomi.com.glrippleview.AnimationUtil;
import r21nomi.com.glrippleview.GLRippleView;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class UtilsSPlaner extends Fragment {
    List<SPlaner>list;
    private static final String DEBUG_TAG = "MyActivity";
    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE,          // 2
            CalendarContract.Instances.END          // 3
    };
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;
    private static final int PROJECTION_END_INDEX = 3;
    RecyclerView rcvListListApp;
    ImageView imgAddListApp;
    private TextView tvToday;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splaner_utils,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initView(view);
        initEvent();
        getRemind();
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

    private void initEvent() {
        imgAddListApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToCent();
            }
        });
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime = Calendar.getInstance().getTime();
//        tvToday.setText(String.valueOf(fmt.format(Calendar.getInstance())));
        Log.d(TAG, "initEvent1: "+currentTime);
        tvToday.setText(String.valueOf(currentTime).substring(0,10).replace(" ",", "));
    }

    private void initView(View view) {
        imgAddListApp=view.findViewById(R.id.img_addListApp);
        tvToday=view.findViewById(R.id.tv_today);
        tvToday=view.findViewById(R.id.tv_today);
        rcvListListApp=view.findViewById(R.id.listListApp);
        LinearLayoutManager linearLayout=new LinearLayoutManager(getContext());
        rcvListListApp.setLayoutManager(linearLayout);
        rcvListListApp.hasFixedSize();

    }

    private void intentToCent() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);
    }
    public void getRemind(){


// Specify the date range you want to search for recurring
// event instances
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "getRemind: "+currentTime.getDate());

        Calendar beginTime = Calendar.getInstance();
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.getInstance().get(Calendar.YEAR), currentTime.getMonth(), currentTime.getDate(), 23, 59);
        Log.d(TAG, "getRemind: "+Calendar.getInstance().get(Calendar.YEAR));

        long endMillis = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = getContext().getContentResolver();

// The ID of the recurring event whose instances you are searching
// for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[] {"207"};

// Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

// Submit the query
        cur =  cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null,
                null);
        list=new ArrayList<>();
        while (cur.moveToNext()) {
            String title = null;
            long eventID = 0;
            long beginVal = 0;
            long endVal = 0;

            // Get the field values
            eventID = cur.getLong(PROJECTION_ID_INDEX);
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);
            endVal = cur.getLong(PROJECTION_END_INDEX);

            // Do something with the values.
            Log.i(DEBUG_TAG, "Event:  " + title);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            Log.i(DEBUG_TAG, "Date: " + formatter.format(endVal));
            list.add(new SPlaner(title,formatter.format(beginVal)+"-"+formatter.format(endVal),R.drawable.gold_coin_icon));
        }
        Splaner splaner=new Splaner(list,getContext());
        rcvListListApp.setAdapter(splaner);
    }

    private String getCalendarUriBase(Activity act) {

        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = act.managedQuery(calendars, null, null, null, null);
        } catch (Exception e) {
        }
        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = act.managedQuery(calendars, null, null, null, null);
            } catch (Exception e) {
            }
            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }
        }
        return calendarUriBase;
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
