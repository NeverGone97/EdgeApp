package com.nextsol.taipv.edgegalaxy.view.fragment;

import android.Manifest;
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
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.Song;
import com.nextsol.taipv.edgegalaxy.presenter.MusicController;
import com.nextsol.taipv.edgegalaxy.view.adapter.SongAdapter;
import com.nextsol.taipv.edgegalaxy.view.service.MusicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OnlineMusic extends Fragment implements MediaController.MediaPlayerControl {

    private int READ_STORAGE_PERMISSION_REQUEST_CODE=1;
    private LinearLayout linear;
    //song list variables
    private ArrayList<Song> songList;
    private ListView songView;

    //service
    private MusicService musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;

    //controller
    private MusicController controller;

    //activity and playback pause flags
    private boolean paused=false, playbackPaused=false;
    public static OnlineMusic newInstance() {
        
        Bundle args = new Bundle();
        
        OnlineMusic fragment = new OnlineMusic();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song_music,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            requestPermissionForReadExtertalStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkPermissionForReadExtertalStorage();
        //retrieve list view
        songView = (ListView)view.findViewById(R.id.song_list);
        //instantiate list
        songList = new ArrayList<Song>();
        //get songs from device
        getSongList();
        //sort alphabetically by title
        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        //create and set adapter
        SongAdapter songAdt = new SongAdapter(getContext(), songList);
        songView.setAdapter(songAdt);
        //setup controller
        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songPicked(view);
            }
        });
        setController();
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
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_STORAGE_PERMISSION_REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        //connect to the service
        private ServiceConnection musicConnection = new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
                //get service
                musicSrv = binder.getService();
                //pass list
                musicSrv.setList(songList);
                musicBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicBound = false;
            }
        };

        //start and bind the service when the activity starts
        @Override
        public void onStart() {
            super.onStart();
            if(playIntent==null){
                playIntent = new Intent(getContext(), MusicService.class);
                getContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
                getContext().startService(playIntent);
            }
        }

        //user song select
        public void songPicked(View view){
            musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
            musicSrv.playSong();
            if(playbackPaused){
                setController();
                playbackPaused=false;
            }
            controller.show(0);
        }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		//menu item selected
//		switch (item.getItemId()) {
//			case R.id.action_shuffle:
//				musicSrv.setShuffle();
//				break;
//			case R.id.action_end:
//				stopService(playIntent);
//				musicSrv=null;
//				System.exit(0);
//				break;
//		}
//		return super.onOptionsItemSelected(item);
//	}

        //method to retrieve song info from device
        public void getSongList(){
            //query external audio
            ContentResolver musicResolver = getContext().getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
            //iterate over results if valid
            if(musicCursor!=null && musicCursor.moveToFirst()){
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
                    songList.add(new Song(thisId, thisTitle, thisArtist));
                }
                while (musicCursor.moveToNext());
            }
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getAudioSessionId() {
            return 0;
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            if(musicSrv!=null && musicBound && musicSrv.isPng())
                return musicSrv.getPosn();
            else return 0;
        }

        @Override
        public int getDuration() {
            if(musicSrv!=null && musicBound && musicSrv.isPng())
                return musicSrv.getDur();
            else return 0;
        }

        @Override
        public boolean isPlaying() {
            if(musicSrv!=null && musicBound)
                return musicSrv.isPng();
            return false;
        }

        @Override
        public void pause() {
            playbackPaused=true;
            musicSrv.pausePlayer();
        }

        @Override
        public void seekTo(int pos) {
            musicSrv.seek(pos);
        }

        @Override
        public void start() {
            musicSrv.go();
        }

        //set the controller up
        private void setController(){
            controller = new MusicController(getContext());
            //set previous and next button listeners
            controller.setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playNext();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playPrev();
                }
            });
            //set and show
            controller.setMediaPlayer(this);
            controller.setAnchorView(getView().findViewById(R.id.song_list));
            controller.setEnabled(true);
        }

        private void playNext(){
            musicSrv.playNext();
            if(playbackPaused){
                setController();
                playbackPaused=false;
            }
            controller.show(0);
        }

        private void playPrev(){
            musicSrv.playPrev();
            if(playbackPaused){
                setController();
                playbackPaused=false;
            }
            controller.show(0);
        }

        @Override
        public void onPause(){
            super.onPause();
            paused=true;
        }

        @Override
        public void onResume(){
            super.onResume();
            if(paused){
                setController();
                paused=false;
            }
        }

        @Override
        public void onStop() {
            controller.hide();
            super.onStop();
        }

        @Override
        public void onDestroy() {
            getContext().stopService(playIntent);
            musicSrv=null;
            super.onDestroy();
        }

    }
