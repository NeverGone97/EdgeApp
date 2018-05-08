package com.nextsol.taipv.edgegalaxy.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.Constants;
import com.nextsol.taipv.edgegalaxy.model.currentweather.Datum;
import com.nextsol.taipv.edgegalaxy.presenter.ICurrentWeatherView;
import com.nextsol.taipv.edgegalaxy.presenter.WeatherPresenter;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kotlin.Pair;
import r21nomi.com.glrippleview.AnimationUtil;
import r21nomi.com.glrippleview.GLRippleView;

public class WeatherUtils extends Fragment implements ICurrentWeatherView {
    WeatherPresenter weatherPresenter;
    private Dialog progressDialog;
    double lat, lon;
    RelativeLayout relativeLayout;
    ImageView imgIcon;
    TextView tvUV,tvWind,tvTempMin,tvTempMax,tvTempCur,tvCity,tvToday,tvDes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_utils, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherPresenter=new WeatherPresenter(this);
        weatherPresenter.getCurrentWeather(21.027764,105.834160, Constants.key);
        weatherPresenter.getDailyWeather(21.027764,105.834160, Constants.key);
        initView(view);
        event();
        final GLRippleView glRippleView = view.findViewById(R.id.glRippleView);
        GLRippleView.Listener gl = new GLRippleView.Listener() {
            @Override
            public void onTouchEvent(@NotNull MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    glRippleView.setRipplePoint(new Pair(AnimationUtil.INSTANCE.map(event.getX(), 0f, getWidth(), -1f, 1f),
                            AnimationUtil.INSTANCE.map(event.getY(), 0f, getHeight(), -1f, 1f)));
                    float var2 = AnimationUtil.INSTANCE.map(event.getX() / getWidth(), 0.0F, 1.0F, 0.0F, 0.02F);
                    glRippleView.setRippleOffset(var2);
                }
            }
        };
        glRippleView.setListener(gl);
        glRippleView.setFadeDuration(1000);
        glRippleView.startCrossFadeAnimation();
    }

    private void event() {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), com.nextsol.taipv.edgegalaxy.view.activity.WeatherUtils.class);
                startActivity(intent);
            }
        });
    }

    private void initView(View view) {
        imgIcon=view.findViewById(R.id.img_icon);
        tvUV=view.findViewById(R.id.tv_uv);
        tvTempMax=view.findViewById(R.id.tv_temp_max);
        tvTempMin=view.findViewById(R.id.tv_temp_min);
        tvTempCur=view.findViewById(R.id.tv_temp_cur);
        tvCity=view.findViewById(R.id.tv_location);
        tvToday=view.findViewById(R.id.tv_time);
        tvDes=view.findViewById(R.id.tv_descrip);
        relativeLayout=view.findViewById(R.id.ln_layout);
    }

    @Override
    public void onGetSuscess(List<Datum> list) {
        Log.d("currentweather", "onGetSuscess: " + list.get(0).getCountry_code());
        getCurrentWeather(list);
    }

    private void getCurrentWeather(List<Datum> list) {
        closeProgressBar();
        tvCity.setText("Ha Noi");
        tvTempCur.setText(String.valueOf((int) list.get(0).getTemp()) + "º");
        tvDes.setText(list.get(0).getWeather().getDescription());
        if (list.get(0).getUv() < 3) {
            tvUV.setText("Low");
        } else if (3 <= list.get(0).getUv() && list.get(0).getUv() < 6) {
            tvUV.setText("Medium");
        } else if (list.get(0).getUv() >= 6 && list.get(0).getUv() < 8) {
            tvUV.setText("Height");
        } else {
            tvUV.setText("Very Height");
        }
//        tvWind.setText(String.valueOf(list.get(0).getWind_spd()) + "m/s");
        if (list.get(0).getWeather().getCode() < 300) {
            imgIcon.setImageResource(R.drawable.weather_thunderstorm);
        } else if (list.get(0).getWeather().getCode() < 700) {
            imgIcon.setImageResource(R.drawable.weather_rain);
        } else if (list.get(0).getWeather().getCode() < 800) {
            imgIcon.setImageResource(R.drawable.weather_fog);
        } else if (list.get(0).getWeather().getCode() == 800) {
            imgIcon.setImageResource(R.drawable.weather_sunny);
        } else if (list.get(0).getWeather().getCode() > 800) {
            imgIcon.setImageResource(R.drawable.weather_partly_cloudy_day);
        }
        Calendar calendar=Calendar.getInstance();
//        String format = "dd/mm/yyyy";
//        SimpleDateFormat sdf=new SimpleDateFormat(format, Locale.US);
//        String today=sdf.format(calendar);
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            int month=calendar.get(Calendar.MONTH)+1;
            int year=calendar.get(Calendar.YEAR);
        tvToday.setText(day+"/"+month+"/"+year);
    }

    @Override
    public void onGetHour(List<com.nextsol.taipv.edgegalaxy.model.fortyeighthour.Datum> list) {
    }

    @Override
    public void onGetDaily(List<com.nextsol.taipv.edgegalaxy.model.sixteendayforecast.Datum> list) {
        tvTempMax.setText(String.valueOf((int) list.get(0).getMax_temp())+"ºC");
        tvTempMin.setText(String.valueOf((int) list.get(0).getMin_temp())+"ºC");
    }

    @Override
    public void showProgressBar(int type) {
        showProgressBar(false,false,"Please waiting ...");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onError(int code) {

    }
    protected void showProgressBar(boolean isTouchOutside, boolean isCancel, String message) {
        try {
            progressDialog = new Dialog(getActivity(), R.style.Theme_Transparent);
            progressDialog.setContentView(R.layout.dialog_progress_bar);
            //noinspection ConstantConditions
            progressDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            progressDialog.setCancelable(isTouchOutside);
            progressDialog.setCanceledOnTouchOutside(isCancel);

            if (message != null) {
                TextView textView = progressDialog.findViewById(R.id.txt_message);
                textView.setText(message);
            }

            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void closeProgressBar() {
        if (progressDialog != null)
            progressDialog.dismiss();
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
