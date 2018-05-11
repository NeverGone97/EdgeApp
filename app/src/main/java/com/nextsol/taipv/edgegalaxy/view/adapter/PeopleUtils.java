package com.nextsol.taipv.edgegalaxy.view.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
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
import com.nextsol.taipv.edgegalaxy.callback.IPassPos;
import com.nextsol.taipv.edgegalaxy.model.PeopleContact;
import com.nextsol.taipv.edgegalaxy.utils.SharePre;

import java.util.List;

public class PeopleUtils extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    IPassPos iPassPos;
    private Context context;
    private List<PeopleContact> list;
    private int currentBackgroundColor = 0xffffffff;
    public static final int PICK_CONTACT = 2;
    SharePre sharePre;
    Gson gson;
    public PeopleUtils(Context context, List<PeopleContact> list, IPassPos iPassPos) {
        this.context = context;
        this.list = list;
        this.iPassPos=iPassPos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.item_people_edge, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemHolder itemHolder = (ItemHolder) holder;
        final PeopleContact contact = list.get(position);
        itemHolder.imgColorName.setImageResource(contact.getColor());
        itemHolder.imgPickColor.setImageResource(contact.getColor());
        itemHolder.imgPickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColor(itemHolder);
            }
        });
        itemHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                // Changes the textview's text to "Checked: example radiobutton text"
                hideColor(itemHolder);
                itemHolder.imgColorName.setImageDrawable(checkedRadioButton.getBackground());
            }
        });
        itemHolder.radMutilColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Choose color")
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(9)
                        .alphaSliderOnly()
                        .lightnessSliderOnly()
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                Toast.makeText(context, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                            changeBackgroundColor(selectedColor);
                                hideColor(itemHolder);
                                itemHolder.imgColorName.setColorFilter(selectedColor);
                                itemHolder.imgPickColor.setColorFilter(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                hideColor(itemHolder);
                            }
                        })
                        .build()
                        .show();
            }
        });
        itemHolder.tvContactPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentContact();
                iPassPos.iPassPoss(position);
            }
        });
            itemHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    list.set(position,new PeopleContact(contact.getColor(),"","Add contacts"));
                    sharePre=new SharePre(context);
                    gson=new Gson();
                    String type=gson.toJson(list);
                    sharePre.saveListContact(type);
                    itemHolder.tvCharFirstName.setVisibility(View.VISIBLE);
                    itemHolder.tvCharFirstName.setText("+");
                    itemHolder.imgColorName.setImageResource(contact.getColor());
                    itemHolder.tvContactPeople.setText("Add contacts");
                    itemHolder.imgDelete.setVisibility(View.GONE);
                }
            });
        if (contact.getName() != null){

            itemHolder.tvContactPeople.setText(contact.getName());
            itemHolder.imgDelete.setVisibility(View.VISIBLE);
            itemHolder.tvCharFirstName.setVisibility(View.VISIBLE);
            if (!contact.getName().equals("Add contacts")){
                itemHolder.tvCharFirstName.setText(contact.getName().substring(0,1));
            }else {
                itemHolder.tvCharFirstName.setText("+");
                itemHolder.imgDelete.setVisibility(View.GONE);
            }
        }

        else {
            itemHolder.tvContactPeople.setText("Add contacts");
        }
        if(contact.getBitmap()!=null){
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

    private void showColor(ItemHolder itemHolder) {
        itemHolder.layoutRadio.setVisibility(View.VISIBLE);
        itemHolder.imgPickColor.setVisibility(View.GONE);
        itemHolder.tvContactPeople.setVisibility(View.GONE);
    }

    private void hideColor(ItemHolder itemHolder) {
        itemHolder.layoutRadio.setVisibility(View.GONE);
        itemHolder.imgPickColor.setVisibility(View.VISIBLE);
        itemHolder.tvContactPeople.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgColorName, imgPickColor, imgDelete, imgExpand;
        private TextView tvContactPeople, tvCharFirstName;
        private RadioButton radMutilColor;
        private LinearLayout layoutRadio;
        private RadioGroup radioGroup;
        private RadioButton checkedRadioButton;

        public ItemHolder(View itemHolder) {
            super(itemHolder);
            imgColorName = itemView.findViewById(R.id.img_color_name);
            imgPickColor = itemView.findViewById(R.id.img_showcolor);
            radMutilColor = itemView.findViewById(R.id.img_multi_color);
            imgDelete = itemView.findViewById(R.id.img_delete_contact);
            imgExpand = itemView.findViewById(R.id.img_arrow_right);
            tvContactPeople = itemView.findViewById(R.id.tv_name_contact);
            tvCharFirstName = itemView.findViewById(R.id.tv_first_name_contact);
            layoutRadio = itemHolder.findViewById(R.id.layout_radio);
            radioGroup = itemHolder.findViewById(R.id.rad_group);
            checkedRadioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

        }
    }
public void setItemClick(IPassPos iPassPos){
        this.iPassPos=iPassPos;
}
    public void updateItem(int pos, PeopleContact contact) {
        list.set(pos, contact);
        notifyDataSetChanged();
    }
}
