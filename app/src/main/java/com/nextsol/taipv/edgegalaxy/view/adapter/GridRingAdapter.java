package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.IPassPos;
import com.nextsol.taipv.edgegalaxy.model.Ring;
import com.nextsol.taipv.edgegalaxy.view.activity.BigerImage;

import java.util.List;

public class GridRingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Ring>list;
    Context context;
    IPassPos iPassPos;
    MediaPlayer mediaPlayer;

    int layout;
    public GridRingAdapter(List<Ring> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_ringtone,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final ItemHolder itemHolder= (ItemHolder) holder;
            final Ring ring=list.get(position);
            itemHolder.imgRing.setImageResource(ring.getIcon());
        itemHolder.tv_ring.setText(ring.getTitle());
        itemHolder.imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

            if(mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.release();
                itemHolder.imgPause.setVisibility(View.GONE);
            }
                     mediaPlayer=MediaPlayer.create(context,ring.getRing());

                    if(mediaPlayer.isPlaying()){
                    itemHolder.imgPlay.setImageResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                    }else {

                        itemHolder.imgPause.setVisibility(View.VISIBLE);
                        itemHolder.imgPlay.setVisibility(View.GONE);
                        mediaPlayer.start();
                    }
                }
            });
        itemHolder.imgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null){
                    mediaPlayer.pause();
                    itemHolder.imgPause.setVisibility(View.GONE);
                    itemHolder.imgPlay.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void intentToBiggerImage(int icon) {
        Intent intent=new Intent(context,BigerImage.class);
        intent.putExtra("icon",icon);
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
            imgPlay=view.findViewById(R.id.img_play_ring);
            imgSetRingtone=view.findViewById(R.id.img_set_ring);
            imgDownload=view.findViewById(R.id.img_down_ring);
            imgRing=view.findViewById(R.id.img_ring);
            tv_ring=view.findViewById(R.id.tv_ring);
            imgPause=view.findViewById(R.id.img_pause_ring);
        }
    }
}
