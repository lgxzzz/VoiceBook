package com.voice.book.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.voice.book.R;
import com.voice.book.adpater.DailySummaryAdapter;
import com.voice.book.adpater.SummaryAdapter;
import com.voice.book.bean.Budget;
import com.voice.book.bean.DailySummary;
import com.voice.book.data.DBManger;
import com.voice.book.util.DateUtil;
import com.voice.book.view.DatePickDialog;
import com.voice.book.view.LeftSwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class DetaildFragment extends AppCompatDialogFragment{

    private TextView mYearTv;
    private TextView mMonthTv;
    private TextView mIncomeTv;
    private TextView mExpenseTv;
    private DatePickDialog mDatePickDialog;

    private LeftSwipeMenuRecyclerView mDailyListview;
    private SummaryAdapter mAdapter;

    private Handler mHandler= new Handler();

    private List<DailySummary> mAlldailySummaries = new ArrayList<>();
    private List<DailySummary> mSelectDateSummaries = new ArrayList<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragement_detail, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initAllData();
    }

    public static DetaildFragment getInstance() {
        return new DetaildFragment();
    }

    public void initView(View view){
        mDailyListview = view.findViewById(R.id.budget_listview);

        mYearTv =view.findViewById(R.id.year_tv);
        mMonthTv =view.findViewById(R.id.month_tv);
        mIncomeTv =view.findViewById(R.id.income_tv);
        mExpenseTv =view.findViewById(R.id.expense_tv);
        mYearTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePickDialog.show();
            }
        });

        mDatePickDialog = new DatePickDialog(getContext(),R.layout.dialog_date,true,true);
        mDatePickDialog.setlistener(new DatePickDialog.IOnSelectListener() {
            @Override
            public void onSelect(final int[] date) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String datestr = date[0]+"年"+date[1]+"月";
                        Toast.makeText(getContext(),"选择月份："+datestr,Toast.LENGTH_LONG).show();
                        mYearTv.setText(date[0]+"年");
                        mMonthTv.setText(date[1]+"月");
                        refresListByMonth(datestr);
                        refreshDataByMonth(datestr);
                    }
                });
            }
        });
    };


    public void initAllData(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mAlldailySummaries.clear();
                mAlldailySummaries = DBManger.getInstance(getContext()).getAllDailyData();
                refresListByMonth(DateUtil.getCurrentMonthStr());
                refreshDataByMonth(DateUtil.getCurrentMonthStr());
            }
        });
    };

    //根据月份查询收入支出
    public void refreshDataByMonth(String select_date){
        int income = 0;
        int expense = 0;
        for (int i=0;i<mAlldailySummaries.size();i++){
            DailySummary summary = mAlldailySummaries.get(i);
            List<Budget> budgets = summary.getmBudgets();
            String date = summary.getDate();
            if (date.contains(select_date)){
                int in = summary.getIncome();
                int ex = summary.getExpense();
                income = in+income;
                expense = ex + expense;
            }
        }
        mIncomeTv.setText(income+"元");
        mExpenseTv.setText(expense+"元");

    }

    //根据月份查询收入支出
    public void refresListByMonth(String select_date){
        mSelectDateSummaries.clear();
        for (int i=0;i<mAlldailySummaries.size();i++){
            DailySummary summary = mAlldailySummaries.get(i);
            List<Budget> budgets = summary.getmBudgets();
            String date = summary.getDate();
            if (date.contains(select_date)){
                mSelectDateSummaries.add(summary);
            }
        }
        mDailyListview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SummaryAdapter(getContext(),mSelectDateSummaries);
        mDailyListview.setAdapter(mAdapter);
        mDailyListview.setOnItemActionListener(new LeftSwipeMenuRecyclerView.OnItemActionListener() {
            //点击
            @Override
            public void OnItemClick(int position) {
                Toast.makeText(getContext(),"Click"+position,Toast.LENGTH_SHORT).show();
            }
            //置顶
            @Override
            public void OnItemTop(int position) {

            }
            //删除
            @Override
            public void OnItemDelete(int position) {
                DBManger.getInstance(getContext()).deleteBudegetByDialy(mSelectDateSummaries.get(position));

                initAllData();
            }
        });
    }
}
