package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.IpassView;
import com.nextsol.taipv.edgegalaxy.callback.ItemClickListener;
import com.nextsol.taipv.edgegalaxy.model.Song;
import com.nextsol.taipv.edgegalaxy.view.service.MusicService;

import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ItemClickListener itemClickListener;
    IpassView ipassView;
    private List<Song>list;
    private Context context;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    public LocalMusicAdapter(List<Song> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_local_music,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ItemHolder itemHolder= (ItemHolder) holder;
        final Song song=list.get(position);
        itemHolder.tvTitle.setText(song.getTitle());
        itemHolder.tvArtist.setText(song.getArtist());
        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemClickListener.onItemClickSong(position,v);
                Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();
                ipassView.onPassView(position,v);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public void  setItemClickSelected(ItemClickListener itemClickSelected){
        this.itemClickListener=itemClickSelected;
    }
    public void setPassView(IpassView ipassView){
        this.ipassView=ipassView;
    }
    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgMusicItem,imgPlay,imgPause,imgNext,imgMore;
        private TextView tvTitle,tvArtist;
        public ItemHolder(View inflate) {
            super(inflate);
            tvTitle=inflate.findViewById(R.id.tv_song_item);
            tvArtist=inflate.findViewById(R.id.tv_author_item);
            imgMusicItem=inflate.findViewById(R.id.img_music_item);
//            imgNext=inflate.findViewById(R.id.img_next);
//            imgPlay=inflate.findViewById(R.id.img_play_music);
//            imgPause=inflate.findViewById(R.id.img_pause_music);
            imgMore=inflate.findViewById(R.id.more_music);
        }
    }
}
