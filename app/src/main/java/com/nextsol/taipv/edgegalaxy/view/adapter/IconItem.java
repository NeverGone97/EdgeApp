package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.IconIcon;

import java.util.List;

public class IconItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<IconIcon>list;
    Context context;

    public IconItem(List<IconIcon> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        return new ItemHolder(layoutInflater.inflate(R.layout.item_icon,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ItemHolder itemHolder= (ItemHolder) holder;
            IconIcon icon=list.get(position);
            itemHolder.iconView.setImageResource(icon.getIcon() );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView iconView;
        public ItemHolder(View inflate) {
            super(inflate);
            iconView=itemView.findViewById(R.id.icon_item);
        }
    }
}
