package com.voice.book.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    //数据库名称
    public static final String DB_NAME = "VoiceBook.db";
    //数据库版本号
    public static int DB_VERSION = 5;
    //用户表
    public static final String TAB_USER = "UserInfo";
    //收支表
    public static final String TAB_BUDGET = "Budget";
    //收支类型表
    public static final String TAB_BUDGET_TYPE = "BudgetType";

    public SQLiteDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableUser(db);
        createTableBudget(db);
        createTableBudgetType(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TAB_USER);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_BUDGET_TYPE);
        onCreate(db);
    }

    //创建用户表
    public void createTableUser(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_USER +
                "(UserId varchar(60) primary key, " +
                "UserName varchar(60), " +
                "Password varchar(60))");
    }

    //创建收支表
    public void createTableBudget(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_BUDGET +
                "(BudegetId integer primary key autoincrement, " +
                "date varchar(60), " +           //日期
                "type varchar(60), " +           // 支出 收入
                "BudegetTypeId varchar(60), " +  //收支类型
                "note varchar(60), " +           //备注
                "num varchar(60))");             //金额
    }

    //创建收支类型表
    public void createTableBudgetType(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_BUDGET_TYPE +
                "(BudegetTypeId varchar(60) primary key, " +
                "type varchar(60), " +   // 支出 收入
                "note varchar(60))");    // 服饰 购物等
    }

}
