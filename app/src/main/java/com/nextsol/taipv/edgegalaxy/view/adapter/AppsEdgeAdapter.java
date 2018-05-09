package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.nextsol.taipv.edgegalaxy.callback.ItemClickListener;
import com.nextsol.taipv.edgegalaxy.model.AppsEdge;
import com.nextsol.taipv.edgegalaxy.view.activity.ShowListApp;

import java.util.List;

public class AppsEdgeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    IPassPos iPassPos;
    ItemClickListener itemClickListener;
    Context context;
    String name;
    List<AppsEdge> list;
    private TextView txt;

    public AppsEdgeAdapter(Context context, List<AppsEdge> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_edge_apps, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        final AppsEdge appsEdge = list.get(position);
//        iPassPos.iPassPoss(position);
        itemHolder.imgAddApp.setImageBitmap(appsEdge.getIcon());
        itemHolder.imgDeleteApp.setImageResource(appsEdge.getDelete());
//            if (itemHolder.tvNameApp.getText().toString()!=""||itemHolder.tvNameApp.getText().toString()!=null){
//                itemHolder.imgDeleteApp.setVisibility(View.VISIBLE);
//            }
//        itemHolder.imgDeleteApp.setVisibility(View.VISIBLE);
        if (appsEdge.getNameApp().length()>10){
            itemHolder.tvNameApp.setText(appsEdge.getNameApp().substring(0,10));
        }else {
            itemHolder.tvNameApp.setText(appsEdge.getNameApp());

        }
        itemHolder.imgAddApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    iPassPos.iPassPoss(position);
                Toast.makeText(context, "" + list.size(), Toast.LENGTH_SHORT).show();
                intentResult(position);
//                    itemHolder.imgAddApp.setImageBitmap(appsEdge1.getIcon());
//                    if (name != "" || name != null) {
//                        itemHolder.tvNameApp.setVisibility(View.VISIBLE);
//                        itemHolder.imgDeleteApp.setVisibility(View.VISIBLE);
//                        itemHolder.imgAddApp.setImageBitmap(appsEdge.getIcon());
//                        itemHolder.tvNameApp.setText(appsEdge.getNameApp());
//                        itemClickListener.onItemClick(position, appsEdge);
//                    }
            }
        });
        itemHolder.imgDeleteApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.plus_btn_selected);
                list.set(position,new AppsEdge("",icon));
                itemHolder.imgAddApp.setImageResource(R.drawable.plus_btn_selected);
                itemHolder.imgDeleteApp.setVisibility(View.GONE);
                itemHolder.tvNameApp.setText("");

            }
        });
    }

    private void intentResult(int pos) {
        Intent intent = new Intent(context, ShowListApp.class);
        intent.putExtra("pos", pos);
        ((Activity) context).startActivityForResult(intent, 1);

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public void onClick(IPassPos iPassPos){
        this.iPassPos=iPassPos;
    }
    private class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView imgAddApp, imgDeleteApp;
        public TextView tvNameApp;


        public ItemHolder(View itemHolder) {
            super(itemHolder);
            imgAddApp = itemHolder.findViewById(R.id.img_add_app);
            tvNameApp = itemHolder.findViewById(R.id.tv_name_app);
            imgDeleteApp = itemHolder.findViewById(R.id.img_delete_app);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            byte[] b = extras.getByteArray("icon");
            String a = extras.getString("name");
            Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
            Log.d("MyAdapter", "onActivityResult" + bmp.toString() + a);
            name = a;
            Toast.makeText(context, "" + name, Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem(int pos, AppsEdge appsEdge) {
        list.add(pos, appsEdge);
        notifyDataSetChanged();
    }

    public void upDateIem(int pos, AppsEdge appsEdge) {
        list.set(pos, appsEdge);
        notifyDataSetChanged();
    }
}
