package com.nextsol.taipv.edgegalaxy.view.activity;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.IPassPos;
import com.nextsol.taipv.edgegalaxy.model.PeopleContact;
import com.nextsol.taipv.edgegalaxy.utils.SharePre;
import com.nextsol.taipv.edgegalaxy.view.adapter.PeopleAdapter;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.nextsol.taipv.edgegalaxy.view.adapter.PeopleAdapter.PICK_CONTACT;

public class PeopleEdge extends AppCompatActivity implements IPassPos {
    private static final String TAG = "xxx";
    int pos = -1;
    private Uri uriContact;
    private String contactID;
    Gson gson;
    Bitmap photo = null;
    String contactName = null;
    String contactNumber = null;
    SharePre sharePre;
    PeopleAdapter adapter;
    RecyclerView recyclerView;
    Integer[] listColor = {R.drawable.circle_cornor_oran, R.drawable.circle_cornor_yellow, R.drawable.circle_cornor_green,
            R.drawable.circle_cornor_blue2, R.drawable.circle_cornor_blue, R.drawable.circle_cornor_red,
            R.drawable.circle_cornor_oran, R.drawable.circle_cornor_yellow, R.drawable.circle_cornor_green,
            R.drawable.circle_cornor_blue2, R.drawable.circle_cornor_blue, R.drawable.circle_cornor_red};
    List<PeopleContact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_edge);
        checkPermissionForReadExtertalStorage();
        try {
            requestPermissionForReadExtertalStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gson=new Gson();
        sharePre=new SharePre(this);
        getContactList();
        initView();
        retrieveContactPhoto();

    }

    private void getContactList() {
        String listContact=sharePre.getListContact();
        Type type=new TypeToken<List<PeopleContact>>(){}.getType();
        contacts=gson.fromJson(listContact,type);
        if(contacts==null){
            contacts=new ArrayList<>();

        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_CONTACTS);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.rcv_people_edge);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        getContactList();
        for (int i = 0; i < listColor.length; i++) {
            contacts.add(new PeopleContact(listColor[i]));
        }
        adapter = new PeopleAdapter(this, contacts, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                uriContact = data.getData();
                retrieveContactName();
                retrieveContactNumber();
                retrieveContactPhoto();
                Log.d(TAG, "onActivityResult:xxx " + photo);
                if (photo != null) {
//                    adapter.updateItem(pos, new PeopleContact(contactNumber, contactName, photo));
                    contacts.set(pos,new PeopleContact(contactNumber, contactName, photo));
                    String json=gson.toJson(contacts);
                    sharePre.saveListContact(json);
                    adapter.notifyDataSetChanged();
                    photo=null;
                } else {
//                    adapter.updateItem(pos, new PeopleContact(listColor[pos], contactNumber, contactName));
                    contacts.set(pos,new PeopleContact(listColor[pos],contactNumber,contactName));
                    String json=gson.toJson(contacts);
                    sharePre.saveListContact(json);
                    adapter.notifyDataSetChanged();
                }
        }

    }


    private void retrieveContactPhoto() {


        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
//                ImageView imageView = (ImageView) findViewById(R.id.img_contact);
//                imageView.setImageBitmap(photo);
                Log.d(TAG, "retrieveContactPhoto: " + photo);
            }

            assert inputStream != null;
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void retrieveContactNumber() {


        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    private void retrieveContactName() {


        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

    }


    @Override
    public void iPassPoss(int pos) {
        Log.d(TAG, "iPassPoss: " + pos);
        this.pos = pos;
    }
}