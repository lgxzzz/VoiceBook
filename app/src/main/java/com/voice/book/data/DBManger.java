package com.voice.book.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.voice.book.bean.Budget;
import com.voice.book.bean.BudgetType;
import com.voice.book.bean.DailySummary;
import com.voice.book.bean.User;
import com.voice.book.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DBManger {
    private Context mContext;
    private SQLiteDbHelper mDBHelper;
    public User mUser;

    public static  DBManger instance;

    public static DBManger getInstance(Context mContext){
        if (instance == null){
            instance = new DBManger(mContext);
        }
        return instance;
    };

    public DBManger(Context mContext){
        this.mContext = mContext;
        mDBHelper = new SQLiteDbHelper(mContext);
        createDefaultBudgetType();
    }


    //用户登陆
    public void login(String name,String password,IListener listener){
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from UserInfo where UserName =? and Password=?",new String[]{name,password});
            if (cursor.moveToFirst()){
                String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
                String UserName = cursor.getString(cursor.getColumnIndex("UserName"));

                mUser = new User();
                mUser.setUserId(UserId);
                mUser.setUserName(UserName);
                listener.onSuccess();
            }else{
                listener.onError("未查询到该用户");
            }
            db.close();
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        listener.onError("未查询到该用户");
    }

    //修改用户信息
    public void updateUser(User user,IListener listener){
        try{
            ContentValues values = new ContentValues();
            values.put("UserName",user.getUserName());
            values.put("Password",user.getPassword());
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int code = db.update(SQLiteDbHelper.TAB_USER,values,"UserId =?",new String[]{user.getUserId()+""});
            listener.onSuccess();
        }catch (Exception e){

        }
    }

    //注册用户
    public void registerUser(User user,IListener listener){
        try{
            ContentValues values = new ContentValues();
            values.put("UserName",user.getUserName());
            values.put("Password",user.getPassword());
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            long code = db.insert(SQLiteDbHelper.TAB_USER,null,values);
            listener.onSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }

    };


    //生成默认的收支类型数据
    public void createDefaultBudgetType(){
        boolean isFirst = SharedPreferenceUtil.getFirstTimeUse(mContext);
        if (isFirst){
            List<String> ExpenseType = new ArrayList<>();
            ExpenseType.add("购物");
            ExpenseType.add("饮食");
            ExpenseType.add("服饰");

            for (int i=0;i<ExpenseType.size();i++){
                BudgetType budgetType = new BudgetType();
                budgetType.setNote(ExpenseType.get(i));
                budgetType.setType("支出");
                budgetType.setBudegetTypeId(getRandomBudgettypeId());
                insertBudgetType(budgetType);
            }

            List<String> IncomeType = new ArrayList<>();
            IncomeType.add("工资");
            IncomeType.add("兼职");
            IncomeType.add("奖金");

            for (int i=0;i<IncomeType.size();i++){
                BudgetType budgetType = new BudgetType();
                budgetType.setNote(IncomeType.get(i));
                budgetType.setType("收入");
                budgetType.setBudegetTypeId(getRandomBudgettypeId());
                insertBudgetType(budgetType);
            }
            SharedPreferenceUtil.setFirstTimeUse(false,mContext);
        }
    }

    public void insertBudgetType(String type,String note){
        BudgetType budgetType =new BudgetType();
        budgetType.setType(type);
        budgetType.setNote(note);
        budgetType.setBudegetTypeId(getRandomBudgettypeId());
        insertBudgetType(budgetType);
    }

    public void updateBudget(Budget budget){
        try{


        }catch (Exception e){
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();
        values.put("BudegetId",getRandomBudgetId());
        values.put("date",budget.getDate());
        values.put("type",budget.getType());
        values.put("BudegetTypeId",budget.getBudegetTypeId());
        values.put("note",budget.getNote());
        values.put("num",budget.getNum());
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int code = db.update(SQLiteDbHelper.TAB_BUDGET,values,"BudegetId =?",new String[]{budget.getBudegetId()+""});
        int x = code;
    }

    //添加收支数据
    public void insertBudget(Budget budge,IListener listener){
        try{
            ContentValues values = new ContentValues();
            values.put("BudegetId",getRandomBudgetId());
            values.put("date",budge.getDate());
            values.put("type",budge.getType());
            values.put("BudegetTypeId",budge.getBudegetTypeId());
            values.put("note",budge.getNote());
            values.put("num",budge.getNum());
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            long code = db.insert(SQLiteDbHelper.TAB_BUDGET,null,values);
            listener.onSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //添加收支类型数据
    public void insertBudgetType(BudgetType budgetType){
        try{
            ContentValues values = new ContentValues();
            values.put("type",budgetType.getType());
            values.put("BudegetTypeId",budgetType.getBudegetTypeId());
            values.put("note",budgetType.getNote());
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            long code = db.insert(SQLiteDbHelper.TAB_BUDGET_TYPE,null,values);
            Log.e("","");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //生成随机10位的budgettypeid
    public String getRandomBudgettypeId(){
        String strRand="" ;
        for(int i=0;i<10;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }

    //生成随机10位的budgetid
    public String getRandomBudgetId(){
        String strRand="" ;
        for(int i=0;i<10;i++){
            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }

    //获取所有的收支记录
    public List<Budget> getAllBudgetData(){
        List<Budget> budgets = new ArrayList<>();
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.query(SQLiteDbHelper.TAB_BUDGET,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                String BudegetId = cursor.getString(cursor.getColumnIndex("BudegetId"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String BudegetTypeId = cursor.getString(cursor.getColumnIndex("BudegetTypeId"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String num = cursor.getString(cursor.getColumnIndex("num"));

                Budget budget = new Budget();
                budget.setBudegetTypeId(BudegetTypeId);
                budget.setBudegetId(BudegetId);
                budget.setType(type);
                budget.setNote(note);
                budget.setNum(num);
                budget.setDate(date);

                budgets.add(budget);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return budgets;
    }

    //获取所有的日期的汇总数据
    public List<DailySummary> getAllDailyData(){
        List<DailySummary> dailySummaries = new ArrayList<>();
        HashMap<String,List<Budget>> mTempData = new HashMap<>();
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.query(SQLiteDbHelper.TAB_BUDGET,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                String BudegetId = cursor.getString(cursor.getColumnIndex("BudegetId"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String BudegetTypeId = cursor.getString(cursor.getColumnIndex("BudegetTypeId"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String num = cursor.getString(cursor.getColumnIndex("num"));

                Budget budget = new Budget();
                budget.setBudegetTypeId(BudegetTypeId);
                budget.setBudegetId(BudegetId);
                budget.setType(type);
                budget.setNote(note);
                budget.setNum(num);
                budget.setDate(date);

                if (!mTempData.containsKey(date)){
                    List<Budget> budgets = new ArrayList<>();
                    budgets.add(budget);
                    mTempData.put(date,budgets);
                }else{
                    List<Budget> budgets = mTempData.get(date);
                    budgets.add(budget);
                    mTempData.put(date,budgets);
                }
            }
            Iterator<Map.Entry<String, List<Budget>>> iter = mTempData.entrySet()
                    .iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                DailySummary dailySummary = new DailySummary();
                dailySummary.setDate((String) entry.getKey());
                dailySummary.setmBudgets((List<Budget>)entry.getValue());
                dailySummaries.add(dailySummary);
            }
            }catch (Exception e){
            e.printStackTrace();
        }
        return dailySummaries;
    };

    //根据type类型获取收支说明
    public ArrayList<String> getBudgetTypeByKey(String type){
        ArrayList<String> types = new ArrayList<>();
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from BudgetType where type =?",new String[]{type});
            while (cursor.moveToNext()){

                String note = cursor.getString(cursor.getColumnIndex("note"));
                types.add(note);
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return types;
    }

    public String getBudgetTypeIDByNote(String note){
        String BudegetTypeId = null;
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from BudgetType where note =?",new String[]{note});
            while (cursor.moveToNext()){

                BudegetTypeId = cursor.getString(cursor.getColumnIndex("BudegetTypeId"));
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return BudegetTypeId;
    }

    public String getBudgetTypeByID(String BudegetTypeId){
        String note = null;
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from BudgetType where BudegetTypeId =?",new String[]{BudegetTypeId});
            while (cursor.moveToNext()){

                note = cursor.getString(cursor.getColumnIndex("note"));
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return note;
    }

    //获取对应的收支类型数据
    public List<BudgetType> getBudgetTypeByType(String type){
        List<BudgetType> mBudgetType = new ArrayList<>();
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from BudgetType where type =?",new String[]{type});
            while (cursor.moveToNext()){
                String BudegetTypeId = cursor.getString(cursor.getColumnIndex("BudegetTypeId"));
                String note = cursor.getString(cursor.getColumnIndex("note"));

                BudgetType budgetType = new BudgetType();
                budgetType.setBudegetTypeId(BudegetTypeId);
                budgetType.setNote(note);
                budgetType.setType(type);
                mBudgetType.add(budgetType);
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mBudgetType;
    }

    //获取对应的收支类型数据
    public List<BudgetType> getAllBudgetType(){
        List<BudgetType> mBudgetType = new ArrayList<>();
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.query(SQLiteDbHelper.TAB_BUDGET_TYPE,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                String BudegetTypeId = cursor.getString(cursor.getColumnIndex("BudegetTypeId"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String type = cursor.getString(cursor.getColumnIndex("type"));

                BudgetType budgetType = new BudgetType();
                budgetType.setBudegetTypeId(BudegetTypeId);
                budgetType.setNote(note);
                budgetType.setType(type);
                mBudgetType.add(budgetType);
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mBudgetType;
    }

    //根据BudegetTypeId删除数据
    public void deleteBudegetTypeById(String BudegetTypeId){
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int x = db.delete(SQLiteDbHelper.TAB_BUDGET_TYPE,"BudegetTypeId =?",new String[]{BudegetTypeId});
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBudegetByDialy(DailySummary dailySummary){
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            for (int i=0;i<dailySummary.getmBudgets().size();i++){
                Budget budget = dailySummary.getmBudgets().get(i);
                int x = db.delete(SQLiteDbHelper.TAB_BUDGET,"BudegetId =?",new String[]{budget.getBudegetId()});
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBudegetByBudget(Budget budget){
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int x = db.delete(SQLiteDbHelper.TAB_BUDGET,"BudegetId =?",new String[]{budget.getBudegetId()});
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public interface IListener{
        public void onSuccess();
        public void onError(String error);
    };


}
