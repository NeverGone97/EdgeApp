package com.nextsol.taipv.edgegalaxy.utils;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ShowDialogCustom extends AppCompatActivity{

    public void showDialogAdvance(int a) {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        View mView=getLayoutInflater().inflate(a,null);
        alert.setView(mView);
        AlertDialog dialog=alert.create();
        dialog.show();

    }
}
