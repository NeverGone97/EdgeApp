package com.nextsol.taipv.edgegalaxy.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nextsol.taipv.edgegalaxy.R;
import com.nextsol.taipv.edgegalaxy.callback.Constants;
import com.nextsol.taipv.edgegalaxy.callback.IPassPos;
import com.nextsol.taipv.edgegalaxy.model.Contacts;
import com.nextsol.taipv.edgegalaxy.utils.SharePre;

import java.io.ByteArrayOutputStream;

public class IconActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    SharePre sharePre;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.icon_activity);
        sharePre = new SharePre(IconActivity.this);
        initView();
        initEvent();
    }

    private void initEvent() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkRadio = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkRadio.isChecked();
//                Bitmap bitmap = ((BitmapDrawable)checkRadio.getBackground()).getBitmap();
                if (isChecked) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    drawable2Bitmap(checkRadio.getBackground()).compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encode = Base64.encodeToString(b, Base64.DEFAULT);
                    sharePre.putString(Constants.encode, encode);
                    intent(drawable2Bitmap(checkRadio.getBackground()));
                }
            }
        });
    }

    private void initView() {
        radioGroup = findViewById(R.id.rg_icon);

    }

    private void intent(Bitmap resoureImage) {
        Intent intent = new Intent(IconActivity.this, FloatingViewService.class);
        intent.putExtra(Constants.putImage, resoureImage);
        startService(intent);
        finish();
    }

    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
