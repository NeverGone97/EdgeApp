package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.gson.Gson;
import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.Constants;
import com.nextsol.taipv.edgegalaxy.callback.IPassPos;
import com.nextsol.taipv.edgegalaxy.callback.IPassPoss;
import com.nextsol.taipv.edgegalaxy.model.PeopleContact;
import com.nextsol.taipv.edgegalaxy.utils.SharePre;

import java.util.List;

public class PeopleUtils extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //    PassData PD;
    public IPassPoss iPassPos;
    private Context context;
    private List<PeopleContact> list;
    public static final int PICK_CONTACT = 2;

    public PeopleUtils(Context context, List<PeopleContact> list, IPassPoss iPassPos) {
        this.context = context;
        this.list = list;
        this.iPassPos = iPassPos;
    }

    public PeopleUtils(Context context, List<PeopleContact> list) {
        this.context = context;
        this.list = list;
//        this.PD = passData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_people_utils, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        final PeopleContact contact = list.get(position);
        itemHolder.imgColorName.setImageResource(contact.getColor());
        itemHolder.tvUtilsName.setText(contact.getName());
//        itemHolder.tvCharFirstName.setText(contact.getName().substring(0,2));
        if (contact.getName() != null) {
            itemHolder.tvCharFirstName.setText(contact.getName().substring(0, 1));
            itemHolder.imgColorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemHolder.imgPhone.setVisibility(View.VISIBLE);
                    itemHolder.imgMessage.setVisibility(View.VISIBLE);
                }
            });

        } else {
            itemHolder.tvCharFirstName.setText("+");
            itemHolder.imgColorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentContact();
                    iPassPos.iPassPoss(position);
                    SharePre sharePre = new SharePre(context);
                    sharePre.putInt(Constants.position, position);
///                PD.passData(3);
                    Log.d("Pass", "onClick: " + position);
                    itemHolder.imgPhone.setVisibility(View.INVISIBLE);
                    itemHolder.imgMessage.setVisibility(View.INVISIBLE);
                }
            });
        }
        itemHolder.imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+contact.getPhone()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        itemHolder.imgMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"+contact.getPhone()));
                context.startActivity(sendIntent);
            }
        });
        if (contact.getBitmap() != null) {
            itemHolder.imgColorName.setImageBitmap(contact.getBitmap());
            itemHolder.tvCharFirstName.setVisibility(View.GONE);
        }
    }


    //    public void onClick(IPassPos iPassPos){
//        this.iPassPos=iPassPos;
//    }
    private void intentContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        ((Activity) context).startActivityForResult(intent, PICK_CONTACT);
    }


    @Override
    public int getItemCount() {
        return 6;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgColorName, imgPhone, imgMessage;
        private TextView tvCharFirstName, tvUtilsName;

        public ItemHolder(View itemHolder) {
            super(itemHolder);
            tvUtilsName = itemHolder.findViewById(R.id.tv_utils_name);
            imgColorName = itemView.findViewById(R.id.img_color_name);
            tvCharFirstName = itemView.findViewById(R.id.tv_first_name_contact);
            imgPhone = itemHolder.findViewById(R.id.call);
            imgMessage = itemHolder.findViewById(R.id.message);
        }
    }


    public void setItemClick(IPassPoss iPassPos) {
        this.iPassPos = iPassPos;
    }

    public void updateItem(int pos, PeopleContact contact) {
        list.set(pos, contact);
        notifyDataSetChanged();
    }

    public interface PassData {
        void passData(int data);
    }
}

