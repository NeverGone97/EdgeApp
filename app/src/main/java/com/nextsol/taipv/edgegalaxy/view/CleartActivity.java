package com.nextsol.taipv.edgegalaxy.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nextsol.taipv.edgegalaxy.R;
import com.ram.speed.booster.RAMBooster;
import com.ram.speed.booster.interfaces.CleanListener;
import com.ram.speed.booster.interfaces.ScanListener;
import com.ram.speed.booster.utils.ProcessInfo;
import com.roger.catloadinglibrary.CatLoadingView;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CleartActivity extends AppCompatActivity {
    MyAsyncTask myAsyncTask;
    ImageView imgClear;
    Button btnClear;
    private TextView persen;
    TextView usage;
    TextView back_clear;
    private RAMBooster booster;
    private static final String TAG = "Booster.Test";
    CatLoadingView mView;
    RippleBackground rippleBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleart);
        initView();
        myAsyncTask = new MyAsyncTask();
        back_clear=findViewById(R.id.back_clear);
        mView = new CatLoadingView();
        if (booster == null)
            booster = null;
        booster = new RAMBooster(CleartActivity.this);
        booster.setDebug(true);
        booster.startScan(true);
        booster.setScanListener(new ScanListener() {
            @Override
            public void onStarted() {
                Log.d(TAG, "Scan started");
                mView.show(getSupportFragmentManager(), "xxx");
                mView.setCanceledOnTouchOutside(false);
                mView.setText("Cleanning ......");
            }

            @Override
            public void onFinished(long availableRam, long totalRam, List<ProcessInfo> appsToClean) {
                Log.d(TAG, String.format(Locale.US,
                        "Scan finished, available RAM: %dMB, total RAM: %dMB",
                        availableRam, totalRam) + totalRam);
                int usagee = (int) ((totalRam - availableRam) * 100 / totalRam);
                myAsyncTask.execute((usagee + 5), (int) (totalRam - availableRam));

                Log.d(TAG, "onFinished: " + usagee);
//                Log.d(TAG, "onFinished: "+usage+"%");
                List<String> apps = new ArrayList<>();
                for (ProcessInfo info : appsToClean) {
                    apps.add(info.getProcessName());
                }
                Log.d(TAG, String.format(Locale.US,
                        "Going to clean founded processes: %s", Arrays.toString(apps.toArray())));
            }
        });


        initEvent();
    }

    private void initEvent() {
        back_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rippleBackground.stopRippleAnimation();
                RelativeLayout rl = findViewById(R.id.rl_clear);
                rl.setBackgroundColor(getResources().getColor(R.color.bluelight));
                btnClear.setVisibility(View.GONE);
                booster.startClean();
                booster.setCleanListener(new CleanListener() {
                    @Override
                    public void onStarted() {
                        Log.d(TAG, "clean started");
                        mView.show(getSupportFragmentManager(), "xxx");
                    }

                    @Override
                    public void onFinished(long availableRam, long totalRam) {
//                    mView.dismiss();
                        Log.d(TAG, String.format(Locale.US,
                                "clean finished, available RAM: %dMB, total RAM: %dMB",
                                availableRam, totalRam));
                        MyClean myClean = new MyClean();
                        Random random = new Random();
                        int rand = random.nextInt(10) + 10;
                        int total = (int) (rand * totalRam / 100);
                        myClean.execute(rand, total);
                        booster = null;
                    }
                });
            }
        });

    }


    private void initView() {
        imgClear = findViewById(R.id.img_clear);
        btnClear = findViewById(R.id.btn_clear);
        persen = findViewById(R.id.tv_persen);
        usage = findViewById(R.id.tv_usage);
         rippleBackground=(RippleBackground)findViewById(R.id.content);
    }

    private class MyClean extends AsyncTask<Integer, String, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {

            int updateInterval = 50;
            int numUpdates = 2 * 1000 / updateInterval;
            double updateBy = params[0] / (double) numUpdates;

            double count = 0;
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 2 * 1000L) {
                long updateStartTime = System.currentTimeMillis();
//                    System.out.println((int) count);
                Log.d(TAG, "run: " + (int) count);
                publishProgress(String.valueOf((int) count));
                count += updateBy;
                long elapsedTime = System.currentTimeMillis() - updateStartTime;
                try {
                    Thread.sleep(updateInterval - elapsedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return params[1];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mView.dismiss();
            usage.setText(String.valueOf(integer) + " MB Cleaner");
            imgClear.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(String... values) {

            persen.setText(values[0] + " % Faster");
        }

    }

    private class MyAsyncTask extends AsyncTask<Integer, String, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {

            int updateInterval = 50;
            int numUpdates = 2 * 1000 / updateInterval;
            double updateBy = params[0] / (double) numUpdates;

            double count = 0;
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 2 * 1000L) {
                long updateStartTime = System.currentTimeMillis();
//                    System.out.println((int) count);
                Log.d(TAG, "run: " + (int) count);
                publishProgress(String.valueOf((int) count));
                count += updateBy;
                long elapsedTime = System.currentTimeMillis() - updateStartTime;
                try {
                    Thread.sleep(updateInterval - elapsedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return params[1];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mView.dismiss();
            rippleBackground.startRippleAnimation();
            usage.setText(String.valueOf(integer) + " MB usage");
            imgClear.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {

            persen.setText(values[0] + " % usage");
        }
    }
}
