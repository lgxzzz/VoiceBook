package com.voice.book.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.voice.book.LoginActivity;
import com.voice.book.MainActivity;
import com.voice.book.R;
import com.voice.book.RegisterActivity;
import com.voice.book.UpdateActivity;
import com.voice.book.VoiceActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class MenuView extends RelativeLayout{
    private Button mHeadBtn;
    private Button mLoginOutBtn;
    private Button mExcelBtn;
    private Button mPdBtn;

    public MenuView(Context context) {
        super(context);
        init();
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.menu_layout,this,true);
        mHeadBtn = findViewById(R.id.setting_head);
        mLoginOutBtn = findViewById(R.id.setting_logintout);
        mExcelBtn = findViewById(R.id.setting_excel);
        mPdBtn = findViewById(R.id.setting_update_pd);

        mLoginOutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity._this.finish();
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));

            }
        });

        mPdBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), UpdateActivity.class));

            }
        });

        mHeadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                MainActivity._this.startActivityForResult(intent, 1);
            }
        });

        mExcelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity._this.exportExcel();
            }
        });
    }


}
