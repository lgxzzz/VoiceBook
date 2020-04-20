package com.voice.book.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Fill;
import com.github.mikephil.charting.utils.MPPointF;
import com.voice.book.R;
import com.voice.book.adpater.DailySummaryAdapter;
import com.voice.book.bean.Budget;
import com.voice.book.bean.DailySummary;
import com.voice.book.chart.DayAxisValueFormatter;
import com.voice.book.chart.MyValueFormatter;
import com.voice.book.chart.XYMarkerView;
import com.voice.book.data.DBManger;
import com.voice.book.util.DateUtil;
import com.voice.book.view.DatePickDialog;
import com.voice.book.view.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ChartFragment extends Fragment{

    private BarChart chart;

    protected Typeface tfRegular;
    protected Typeface tfLight;

    private PieChart mPiechart;

    private Spinner mTypeSp;

    private TextView mYearTv;

    private DatePickDialog mDatePickDialog;

    private List<DailySummary> mAlldailySummaries = new ArrayList<>();
    private List<DailySummary> mSelectMonthSummaries = new ArrayList<>();

    private String mCurrentBudgetType = "收入";
    private String mCurrentDate = DateUtil.getCurrentDayStr();

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
        tfRegular = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");

        chart = view.findViewById(R.id.chart1);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn


        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);





        mPiechart = view.findViewById(R.id.pie_chart);
        mPiechart.setUsePercentValues(true);
        mPiechart.getDescription().setEnabled(false);
        mPiechart.setExtraOffsets(5, 10, 5, 5);

        mPiechart.setDragDecelerationFrictionCoef(0.95f);

        mPiechart.setCenterTextTypeface(tfLight);


        mPiechart.setDrawHoleEnabled(true);
        mPiechart.setHoleColor(Color.WHITE);

        mPiechart.setTransparentCircleColor(Color.WHITE);
        mPiechart.setTransparentCircleAlpha(110);

        mPiechart.setHoleRadius(58f);
        mPiechart.setTransparentCircleRadius(61f);

        mPiechart.setDrawCenterText(true);

        mPiechart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPiechart.setRotationEnabled(true);
        mPiechart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener

        mPiechart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l2 = mPiechart.getLegend();
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l2.setOrientation(Legend.LegendOrientation.VERTICAL);
        l2.setDrawInside(false);
        l2.setXEntrySpace(7f);
        l2.setYEntrySpace(0f);
        l2.setYOffset(0f);

        // entry label styling
        mPiechart.setEntryLabelColor(Color.WHITE);
        mPiechart.setEntryLabelTypeface(tfRegular);
        mPiechart.setEntryLabelTextSize(12f);


        mTypeSp = view.findViewById(R.id.spinner_type);

        final ArrayList<String> mInExpType=new ArrayList<String>();
        mInExpType.add("收入");
        mInExpType.add("支出");
        SpinnerAdapter adapter = new SpinnerAdapter(getContext(),android.R.layout.simple_spinner_item,mInExpType);
        mTypeSp.setAdapter(adapter);
        mTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshDataByBudgetType(mInExpType.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mYearTv = view.findViewById(R.id.date_tv);

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
                String datestr = date[0]+"年"+date[1]+"月";
                Toast.makeText(getContext(),"选择月份："+datestr,Toast.LENGTH_LONG).show();
                refresDataByMonth(datestr);
            }
        });
    };

    public void refreshDataByBudgetType(String type){
        mCurrentBudgetType = type;
        refreshBarChart();
        refreshPieChart();
    }

    public void refresDataByMonth(String datestr){
        mCurrentDate = datestr;
        mYearTv.setText(datestr);
        mAlldailySummaries.clear();
        mAlldailySummaries = DBManger.getInstance(getContext()).getAllDailyData();
        mSelectMonthSummaries.clear();
        for (int i=0;i<mAlldailySummaries.size();i++){
            DailySummary summary = mAlldailySummaries.get(i);
            List<Budget> budgets = summary.getmBudgets();
            String date = summary.getDate();
            if (date.contains(datestr)){
                mSelectMonthSummaries.add(summary);
            }
        }
        refreshBarChart();
        refreshPieChart();
    };


    public void initData(){
        refresDataByMonth(DateUtil.getCurrentMonthStr());
    };

    //刷新柱形图
    private void refreshBarChart() {

        ArrayList<BarEntry> values = new ArrayList<>();

        int days = DateUtil.getMonthOfDay(mCurrentDate);
        for (int i=1;i<=days;i++){
            int number = caluteNumberByDate(i);
            BarEntry barEntry = new BarEntry(i,number);
            values.add(barEntry);
        }

        // chart.setDrawYLabels(false);
        chart.setMaxVisibleValueCount(days);
        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        ValueFormatter custom = new MyValueFormatter("元");

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, DateUtil.getCurrentMonthStr());

            set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);

            List<Fill> gradientFills = new ArrayList<>();
            gradientFills.add(new Fill(startColor1, endColor1));
            gradientFills.add(new Fill(startColor2, endColor2));
            gradientFills.add(new Fill(startColor3, endColor3));
            gradientFills.add(new Fill(startColor4, endColor4));
            gradientFills.add(new Fill(startColor5, endColor5));

            set1.setFills(gradientFills);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            chart.setData(data);

        }
        chart.invalidate();
    }

    //刷新环形图
    private void refreshPieChart() {
        final ArrayList<String> mParties= DBManger.getInstance(getContext()).getBudgetTypeByKey(mCurrentBudgetType);

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < mParties.size() ; i++) {
            String label = mParties.get(i);
            long number = caluteNumberByBudgetType(label);
            PieEntry pieEntry = new PieEntry(number,label);
            entries.add(pieEntry);
        }

        PieDataSet dataSet = new PieDataSet(entries, "类别明细");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(mPiechart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tfLight);
        mPiechart.setData(data);

        // undo all highlights
        mPiechart.highlightValues(null);
        mPiechart.setCenterText(generateCenterSpannableText());
        mPiechart.invalidate();
    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString(mCurrentBudgetType);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 2, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    //根据收支类别统计一个月的数目
    public long caluteNumberByBudgetType(String type){
        int all_cout = 0;
        for (int i= 0;i<mSelectMonthSummaries.size();i++){
            DailySummary dailySummary = mSelectMonthSummaries.get(i);
            List<Budget> budgets = dailySummary.getmBudgets();
            for (int j= 0;j<budgets.size();j++){
                Budget budget = budgets.get(j);
                String budgettype = DBManger.getInstance(getContext()).getBudgetTypeByID(budget.getBudegetTypeId());
                if (budgettype.equals(type)){
                    all_cout = all_cout+ Integer.parseInt(budget.getNum());
                }
            }
        }
        return all_cout;
    }

    //根据日期获取当天的收支数目
    public int caluteNumberByDate(int day){
        int all_cout = 0;
        for (int i= 0;i<mSelectMonthSummaries.size();i++){
            DailySummary dailySummary = mSelectMonthSummaries.get(i);
            List<Budget> budgets = dailySummary.getmBudgets();
            for (int j= 0;j<budgets.size();j++){
                Budget budget = budgets.get(j);
                String type = budget.getType();
                String date = budget.getDate();
                if (type.equals(mCurrentBudgetType)&&isThisDay(day,date)){
                    all_cout = all_cout+ Integer.parseInt(budget.getNum());
                }
            }
        }
        return all_cout;
    }

    public boolean isThisDay(int day,String date){
        String time = date.replace("年","-").replace("月","-").replace("日","");
        String[] temp = time.split("-");
        int year = Integer.parseInt(temp[0]);
        int month = Integer.parseInt(temp[1]);
        int tempday = Integer.parseInt(temp[2]);
        if (tempday == day){
            return true;
        }
        return false;
    }
}
