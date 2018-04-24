package com.nextsol.taipv.edgegalaxy.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.IpassView;
import com.nextsol.taipv.edgegalaxy.model.Song;
import com.nextsol.taipv.edgegalaxy.presenter.MusicController;
import com.nextsol.taipv.edgegalaxy.view.adapter.LocalMusicAdapter;
import com.nextsol.taipv.edgegalaxy.view.service.MusicService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocalMusic extends Fragment implements View.OnClickListener {
    private int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private LinearLayout linear;
    //song list variables

    private List<Song> list;
    private RecyclerView rcv_local_music;
    private ImageView imgPlay,imgNext;
    private TextView tvSong,tvArtise;
    private RelativeLayout barPlay;
    //service
    private MusicService musicSrv;
    private Intent playIntent;
    int position=-1;
    private SeekBar seekBar;
    //binding
    private boolean musicBound = false;

    //controller
    private MusicController controller;

    //activity and playback pause flags
    private boolean paused = false, playbackPaused = false;
    private ImageView imgPause;

    public static LocalMusic newInstance() {
        Bundle args = new Bundle();

        LocalMusic fragment = new LocalMusic();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestPermissionForReadExtertalStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkPermissionForReadExtertalStorage();
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList((ArrayList<Song>) list);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(getContext(), MusicService.class);
            getContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getContext().startService(playIntent);
        }
    }

    public void songPicked(int pos) {
        position=pos;
        musicSrv.setSong(pos);
        musicSrv.playSong();

//        if (playbackPaused) {
//            setController();
//            playbackPaused = false;
//        }
//        controller.show(0);
//        setController();
//        playbackPaused=true;
//        musicBound=true;
//        isPlaying();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initEvents();
        getSongList();
        Collections.sort(list, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        LocalMusicAdapter adapter = new LocalMusicAdapter(list, getContext());
//        rcv_local_music.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                songPicked(v);
//                Toast.makeText(getContext(), "xxx", Toast.LENGTH_SHORT).show();
//            }
//        });
        rcv_local_music.setAdapter(adapter);
        adapter.setPassView(new IpassView() {
            @Override
            public void onPassView(int pos, View view) {
                Log.d("xxx", "onPassView: " + pos);
                songPicked(pos);
                tvSong.setText(list.get(pos).getTitle());
                tvArtise.setText(list.get(pos).getArtist());
                imgPlay.setVisibility(View.GONE);
                imgPause.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initEvents() {
        imgPlay.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgPause.setOnClickListener(this);
        barPlay.setOnClickListener(this);
        seekBar.setMax(100000);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                musicSrv.seek(progress*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicSrv.seek(seekBar.getProgress());
                Log.d("xxx", "onStopTrackingTouch: "+seekBar.getProgress()+"Max time:"+musicSrv.getTotal());
            }
        });
    }

    private void initView(View view) {
        list = new ArrayList<>();
        rcv_local_music = view.findViewById(R.id.rcv_music_local);
        imgPlay = view.findViewById(R.id.img_play_music);
        imgPause=view.findViewById(R.id.img_pause_music);
        imgNext=view.findViewById(R.id.img_next);
        tvSong=view.findViewById(R.id.tv_name_song);
        tvArtise=view.findViewById(R.id.tv_name_author);
        barPlay=view.findViewById(R.id.relay_bar_play);
        seekBar=view.findViewById(R.id.seek_duration);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rcv_local_music.setLayoutManager(manager);
        rcv_local_music.setHasFixedSize(true);
    }

    //start and bind the service when the activity starts
    public void getSongList() {
        //query external audio
        ContentResolver musicResolver = getContext().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                list.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_play_music:
                imgPause.setVisibility(View.VISIBLE);
                imgPlay.setVisibility(View.GONE);
                if(position!=-1){
                    songPicked(position);
                    updatePost(position);
                }else {
                    songPicked(0);
                    updatePost(0);
                }

//                updatePost(3);
                UpdateTimeSong();
                Log.d("xxx", "onClick: "+musicSrv.getDur());
//                setTimeTotal();
                break;
            case R.id.img_pause_music:
                imgPause.setVisibility(View.GONE);
                imgPlay.setVisibility(View.VISIBLE);
                musicSrv.pausePlayer();
                break;
            case R.id.img_next:
                position++;
                musicSrv.playNext();
                updatePost(position);
                break;
            case R.id.relay_bar_play:
                addFragment(new MusicControll());
                break;
        }
    }

    private void addFragment(Fragment fragment) {

        FragmentTransaction ft=getChildFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_up);
        ft.add(R.id.control_frame,fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void UpdateTimeSong(){
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public static final String TAG = "xxx";

                @Override
                public void run() {
                    SimpleDateFormat formatTime=new SimpleDateFormat("mm:ss");
                    Log.d(TAG, "run: "+formatTime.format(musicSrv.getDur()));
                    //update progress
                    seekBar.setProgress(musicSrv.getDur());
                    Log.d(TAG, "run: "+musicSrv.getDur());
                    mHandler.postDelayed(this,500);
                }
            },100);
    }
    public void updatePost(int position){
        tvSong.setText(list.get(position).getTitle());
        tvArtise.setText(list.get(position).getArtist());
    }

}
