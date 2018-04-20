package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.model.ImageGrid;
import com.nextsol.taipv.edgegalaxy.view.activity.BigerImage;

import java.util.List;

public class GridWallBigerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ImageGrid>list;
    Context context;
    int layout;
    public GridWallBigerAdapter(List<ImageGrid> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_image_grid_biger,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ItemHolder itemHolder= (ItemHolder) holder;
            final ImageGrid imageGrid=list.get(position);
            itemHolder.imageView.setImageResource(imageGrid.getIcon());
            itemHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentToBiggerImage(imageGrid.getIcon());
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
        private ImageView imageView;
        public ItemHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.img_item_grid);
        }
    }
}
