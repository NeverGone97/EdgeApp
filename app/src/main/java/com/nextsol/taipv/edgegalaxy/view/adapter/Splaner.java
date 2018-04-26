package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.SPlaner;

import org.w3c.dom.Text;

import java.util.List;

public class Splaner extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SPlaner>list;
    Context context;

    public Splaner(List<SPlaner> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_splanner,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ItemHolder itemHolder= (ItemHolder) holder;
            SPlaner sPlaner=list.get(position);
        itemHolder.tvTime.setText(sPlaner.getTime());
        itemHolder.tvTitle.setText(sPlaner.getTitle());
        itemHolder.tvTitle.setSelected(true);
        itemHolder.imgDot.setImageResource(sPlaner.getIcon());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvTime,tvTitle;
        ImageView imgDot;
        public ItemHolder(View view) {
            super(view);
            tvTime=view.findViewById(R.id.tv_time_splaner);
            tvTitle=view.findViewById(R.id.tv_title_splaner);
            imgDot=view.findViewById(R.id.img_dot);
        }
    }
}
