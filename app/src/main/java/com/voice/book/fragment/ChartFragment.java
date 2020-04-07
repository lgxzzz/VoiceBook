package com.voice.book.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.voice.book.R;

import java.util.ArrayList;
import java.util.List;


public class ChartFragment extends Fragment{


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragement_chart, container, false);
        initView(view);

        return view;
    }

    public static ChartFragment getInstance() {
        return new ChartFragment();
    }

    public void initView(View view){



    };

    public void initData(){

    };


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
