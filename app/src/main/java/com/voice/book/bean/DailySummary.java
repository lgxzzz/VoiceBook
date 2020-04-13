package com.voice.book.bean;

import java.util.List;

public class DailySummary {
  String date;
  List<Budget> mBudgets;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Budget> getmBudgets() {
        return mBudgets;
    }

    public void setmBudgets(List<Budget> mBudgets) {
        this.mBudgets = mBudgets;
    }

    public String getIncome(){
        return null;
    }

    public String getExpense(){
        return null;
    };
}
