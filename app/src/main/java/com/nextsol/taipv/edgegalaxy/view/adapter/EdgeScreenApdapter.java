package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nextsol.taipv.edgegalaxy.R;

import java.util.List;

public class EdgeScreenApdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Integer> listImage;
    private Context context;

    public EdgeScreenApdapter(List<Integer> listImage, Context context) {
        this.listImage = listImage;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_edge_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.imageScreen.setImageResource(listImage.get(position));
    }

    @Override
    public int getItemCount() {
        return listImage.size() > 0 ? listImage.size() : 0;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imageScreen;

        public ItemHolder(View itemView) {
            super(itemView);
            imageScreen = itemView.findViewById(R.id.img_edge_screen);
        }
    }
}
