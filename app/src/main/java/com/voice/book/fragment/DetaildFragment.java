package com.voice.book.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.voice.book.R;


public class DetaildFragment extends Fragment{

    private Button mStartBtn;
    private Button mEndBtn;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragement_detail, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public static DetaildFragment getInstance() {
        return new DetaildFragment();
    }

    public void initView(View view){

    };

    public void initData(){

    };
}
