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
import android.widget.ListView;

import com.voice.book.R;
import com.voice.book.adpater.DailySummaryAdapter;
import com.voice.book.bean.DailySummary;
import com.voice.book.data.DBManger;

import java.util.List;


public class DetaildFragment extends Fragment{

    private Button mStartBtn;
    private Button mEndBtn;

    private ListView mDailyListview;
    private DailySummaryAdapter mAdapter;
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
        mDailyListview = view.findViewById(R.id.budget_listview);
    };

    public void initData(){
        List<DailySummary> dailySummaries = DBManger.getInstance(getContext()).getDailyData();
        mAdapter = new DailySummaryAdapter(getContext(),dailySummaries);
        mDailyListview.setAdapter(mAdapter);
    };
}
