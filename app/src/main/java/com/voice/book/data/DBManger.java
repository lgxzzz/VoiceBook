package com.voice.book.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.voice.book.bean.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    }


    //用户登陆
    public void login(String name,String password,IListener listener){
        try{
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from UserInfo where UserName =? and Password=?",new String[]{name,password});
            if (cursor.moveToFirst()){
                String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
                String UserName = cursor.getString(cursor.getColumnIndex("UserName"));
                int RoleId = cursor.getInt(cursor.getColumnIndex("RoleId"));
                String Sex = cursor.getString(cursor.getColumnIndex("Sex"));
                String Telephone = cursor.getString(cursor.getColumnIndex("Telephone"));
                String IdCard = cursor.getString(cursor.getColumnIndex("IdCard"));
                String RFIId = cursor.getString(cursor.getColumnIndex("RFIId"));

                mUser = new User();
                mUser.setUserId(UserId);
                mUser.setUserName(UserName);
                mUser.setSex(Sex);
                mUser.setTelephone(Telephone);
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
            values.put("Sex",user.getSex());
            values.put("Telephone",user.getTelephone());
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
            values.put("Sex",user.getSex());
            values.put("Telephone",user.getTelephone());
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            long code = db.insert(SQLiteDbHelper.TAB_USER,null,values);
            listener.onSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }

    };

    public interface IListener{
        public void onSuccess();
        public void onError(String error);
    };


}
