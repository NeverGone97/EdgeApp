package com.nextsol.taipv.edgegalaxy.view;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.IPassPos;
import com.nextsol.taipv.edgegalaxy.callback.IPassPoss;
import com.nextsol.taipv.edgegalaxy.model.PeopleContact;
import com.nextsol.taipv.edgegalaxy.utils.SharePre;
import com.nextsol.taipv.edgegalaxy.view.adapter.PeopleAdapter;
import com.nextsol.taipv.edgegalaxy.view.adapter.PeopleUtils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;
import static com.nextsol.taipv.edgegalaxy.view.adapter.PeopleAdapter.PICK_CONTACT;

public class CallPhoneUtils extends Fragment implements IPassPoss {
    SharePre sharePre;
    PeopleUtils peopleUtils;
    public static final int PICK_CONTACT = 2;
    RecyclerView rcvContact;
    int pos = -1;
    List<PeopleContact> contacts;
    Integer[] listColor = {R.drawable.circle_cornor_oran, R.drawable.circle_cornor_yellow, R.drawable.circle_cornor_green,
            R.drawable.circle_cornor_blue2, R.drawable.circle_cornor_blue, R.drawable.circle_cornor_red,
            R.drawable.circle_cornor_oran, R.drawable.circle_cornor_yellow, R.drawable.circle_cornor_green,
            R.drawable.circle_cornor_blue2, R.drawable.circle_cornor_blue, R.drawable.circle_cornor_red};
    Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePre = new SharePre(getContext());
        gson = new Gson();
        getContactList();

//        peopleUtils.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.call_phone_utils, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        for (int i = 0; i < listColor.length; i++) {
            contacts.add(new PeopleContact(listColor[i]));
        }
        peopleUtils = new PeopleUtils(getContext(), contacts, this);
        peopleUtils.notifyDataSetChanged();
        rcvContact.setAdapter(peopleUtils);
    }

    private void initView(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvContact = view.findViewById(R.id.rcv_contact);
        rcvContact.setLayoutManager(linearLayoutManager);
        rcvContact.setHasFixedSize(true);

    }

    private void getContactList() {
        String listContact = sharePre.getListContact();
        if(!listContact.equals("NullContact")){

        Type type = new TypeToken<List<PeopleContact>>() {
        }.getType();
        contacts = gson.fromJson(listContact, type);
        }

//        Log.d(TAG, "getContactList: "+contacts.size());
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
    }

    @Override
    public void iPassPoss(int pos) {
        Log.d(TAG, "iPassPoss: " + pos);
        this.pos = pos;
    }
}
