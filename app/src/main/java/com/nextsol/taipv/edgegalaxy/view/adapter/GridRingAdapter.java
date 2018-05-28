package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.IPassPos;
import com.nextsol.taipv.edgegalaxy.model.Ring;
import com.nextsol.taipv.edgegalaxy.view.activity.BigerImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class GridRingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Ring> list;
    Context context;
    MediaPlayer mediaPlayer;
    private String fNmae = "lactroi.mp3";
    private String fPAth = "android.resource://com.nextsol.taipv.edgegalaxy/raw/";
    int layout;

    public GridRingAdapter(List<Ring> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_ringtone, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        final Ring ring = list.get(position);
        itemHolder.imgRing.setImageResource(ring.getIcon());
        itemHolder.tv_ring.setText(ring.getTitle());
        itemHolder.imgSetRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setRingtone(ring.getTitle());
                setRingtone(ring.getNameSong(), ring.getTitle());
            }
        });
        itemHolder.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearButton();
                list.set(position, new Ring(list.get(position).getIcon(), list.get(position).getRing(), list.get(position).getTitle(), true, list.get(position).getNameSong()));
                notifyDataSetChanged();
                for (int i = 0; i < list.size(); i++) {
                    Log.d("xxx", "onClick: " + list.get(i).getPlaying());
                }
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    itemHolder.imgPause.setVisibility(View.GONE);
                }
                mediaPlayer = MediaPlayer.create(context, ring.getRing());

                if (mediaPlayer.isPlaying()) {
                    itemHolder.imgPlay.setImageResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                } else {

                    itemHolder.imgPause.setVisibility(View.VISIBLE);
                    itemHolder.imgPlay.setVisibility(View.GONE);
                    mediaPlayer.start();
                }

            }
        });
        if (ring.getPlaying()) {
            itemHolder.imgPlay.setVisibility(View.GONE);
            itemHolder.imgPause.setVisibility(View.VISIBLE);

        } else {
            itemHolder.imgPlay.setVisibility(View.VISIBLE);
            itemHolder.imgPause.setVisibility(View.GONE);
        }
//            notifyDataSetChanged();
        itemHolder.imgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    itemHolder.imgPause.setVisibility(View.GONE);
                    itemHolder.imgPlay.setVisibility(View.VISIBLE);
                }
            }
        });
        itemHolder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyFiletoExternalStorage(ring.getRing(), ring.getNameSong());
            }
        });
    }

    private void intentToBiggerImage(int icon) {
        Intent intent = new Intent(context, BigerImage.class);
        intent.putExtra("icon", icon);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgPlay;
        private ImageView imgSetRingtone;
        private ImageView imgDownload;
        private ImageView imgRing;
        private TextView tv_ring;
        private ImageView imgPause;

        public ItemHolder(View view) {
            super(view);
            imgPlay = view.findViewById(R.id.img_play_ring);
            imgSetRingtone = view.findViewById(R.id.img_set_ring);
            imgDownload = view.findViewById(R.id.img_down_ring);
            imgRing = view.findViewById(R.id.img_ring);
            tv_ring = view.findViewById(R.id.tv_ring);
            imgPause = view.findViewById(R.id.img_pause_ring);
        }
    }

    private void ClearButton() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPlaying(false);
        }
        notifyDataSetChanged();
    }

    private void setRingtone(String name, String title) {
        AssetFileDescriptor openAssetFileDescriptor;
        ((AudioManager) context.getSystemService(AUDIO_SERVICE)).setRingerMode(2);
        File file = new File(Environment.getExternalStorageDirectory() + "/appkeeda", name + ".mp3");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String path = this.fPAth.concat(name);
        Log.e(TAG, "setRingtone: " + path);
        assert path != null;
        Uri parse = Uri.parse(path);
        ContentResolver contentResolver = context.getContentResolver();
        try {
            openAssetFileDescriptor = contentResolver.openAssetFileDescriptor(parse, "r");
        } catch (FileNotFoundException e2) {
            openAssetFileDescriptor = null;
            Log.e(TAG, "setRingtone: " + e2.getMessage());
        }
        try {
            byte[] bArr = new byte[1024];
            assert openAssetFileDescriptor != null;
            FileInputStream createInputStream = openAssetFileDescriptor.createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            for (int read = createInputStream.read(bArr); read != -1; read = createInputStream.read(bArr)) {
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.close();
        } catch (IOException e3) {
            Log.d("xxx", "setRingtone: " + e3.getMessage());
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", file.getAbsolutePath());
        contentValues.put("title", "edgeRing");
        contentValues.put("mime_type", "audio/mp3");
        contentValues.put("_size", Long.valueOf(file.length()));
        contentValues.put("artist", Integer.valueOf(R.string.app_name));
        contentValues.put("is_ringtone", Boolean.valueOf(true));
        contentValues.put("is_notification", Boolean.valueOf(true));
        contentValues.put("is_alarm", Boolean.valueOf(true));
        contentValues.put("is_music", Boolean.valueOf(true));
        try {
            contentResolver.delete(MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), null, null);
            RingtoneManager.setActualDefaultRingtoneUri(context, 1, contentResolver.insert(MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), contentValues));
            Toast.makeText(context, new StringBuilder().append("Ringtone set successfully " + title), Toast.LENGTH_LONG).show();
            Log.d(TAG, "setRingtone: " + MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()) + " : " + contentValues);
        } catch (Throwable th) {
            Toast.makeText(context, new StringBuilder().append("Ringtone feature is not working"), Toast.LENGTH_LONG).show();
        }
    }

    private void copyFiletoExternalStorage(int resourceId, String resourceName) {
        String pathSDCard = Environment.getExternalStorageDirectory() + "/Android/data/" + resourceName + ".mp3";

        try {
            InputStream in = context.getResources().openRawResource(resourceId);
            FileOutputStream out = null;
            out = new FileOutputStream(pathSDCard);
            Log.e(TAG, "copyFiletoExternalStorage: " + pathSDCard);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "This song downloaded.", Toast.LENGTH_SHORT).show();
    }
}
