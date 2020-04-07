package com.voice.book.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    //数据库名称
    public static final String DB_NAME = "VoiceBook.db";
    //数据库版本号
    public static int DB_VERSION = 1;
    //用户表
    public static final String TAB_USER = "UserInfo";
    //用户角色表
    public static final String TAB_ROLE = "Role";
    //车辆信息表
    public static final String TAB_CARINFO = "CarInfo";
    //车库信息表
    public static final String TAB_CARPORTINFO = "CarPortInfo";
    //车位信息表
    public static final String TAB_PARKINGSAPCEINFO = "ParkingSpaceInfo";
    //卡信息表
    public static final String TAB_RFI = "RFIInfo";
    //停车记录信息表
    public static final String TAB_PARKINGINFO = "ParkingInfo";
    //消费表
    public static final String TAB_BILLINFO = "BillInfo";
    //路线信息表
    public static final String TAB_ROUTEINFO = "RouteInfo";
    //用户充值表
    public static final String TAB_ADDMONEYINFO = "AddMoneyInfo";

    public SQLiteDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableUser(db);
//        createTableRole(db);
//        createTableCarInfo(db);
//        createTableCarPortInfo(db);
//        createTableParkingSpaceInfo(db);
//        createTableRFI(db);
//        createTableParkingInfo(db);
//        createTableBillInfo(db);
//        createTableRouteInfo(db);
//        createTableAddMoneyInfo(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TAB_USER);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_ROLE);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_CARINFO);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_CARPORTINFO);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_PARKINGSAPCEINFO);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_RFI);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_PARKINGINFO);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_BILLINFO);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_ROUTEINFO);
//        db.execSQL("DROP TABLE IF EXISTS "+TAB_ADDMONEYINFO);
        onCreate(db);
    }

    //创建用户表
    public void createTableUser(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_USER +
                "(UserId varchar(60) primary key, " +
                "UserName varchar(60), " +
                "Password varchar(60), " +
                "RFIId varchar(60), " +
                "RoleId integer, " +
                "Sex varchar(1), " +
                "Telephone varchar(60), " +
                "IdCard varchar(60), " +
                "UserPhoto text)");
    }

    //创建角色表
    public void createTableRole(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_ROLE +
                "(RoleId integer primary key autoincrement, " +
                "RolerName varchar(60))");
    }

    //创建车辆信息表
    public void createTableCarInfo(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_CARINFO +
                "(CarId varchar(60) primary key, " +
                "UserId varchar(60), " +
                "Type varchar(60), " +
                "CarPhoto text)");
    }

    //创建车库信息表
    public void createTableCarPortInfo(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_CARPORTINFO +
                "(CarPortId varchar(60) primary key, " +
                "CarPortName varchar(60), " +
                "Content integer, " +
                "IsFilled integer, " +
                "IsOrder integer, " +
                "RemainingNumber integer, " +
                "Address varchar(60), " +
                "Price varchar(60), " +
                "OrderPrice varchar(60), " +
                "Induction text, " +
                "CarPortPhoto text)");
    }

    //创建车位信息表
    public void createTableParkingSpaceInfo(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_PARKINGSAPCEINFO +
                "(PlaceId varchar(60) primary key, " +
                "CarPortId integer, " +
                "State varchar(60))");
    }

    //创建RFID卡信息表
    public void createTableRFI(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_RFI +
                "(RFIId varchar(60) primary key, " +
                "UserId varchar(60), " +
                "Remain varchar(60))");
    }

    //创建停车记录信息表
    public void createTableParkingInfo(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_PARKINGINFO +
                "(RecordId varchar(60) primary key, " +
                "CarPortId integer, " +
                "UserId varchar(60), " +
                "CarId varchar(60), " +
                "InTime varchar(60), " +
                "OutTime varchar(60), " +
                "ParkSumTime integer, " +
                "Cost varchar(60), " +
                "ParkingDate varchar(60), " +
                "Way varchar(60), " +
                "Status varchar(60))");
    }

    //创建消费表
    public void createTableBillInfo(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_BILLINFO +
                "(BillId varchar(60) primary key, " +
                "CarPortId integer, " +
                "RecordId integer, " +
                "UserId varchar(60), " +
                "BillDate varchar(60), " +
                "Cost varchar(60), " +
                "PayWay varchar(60))");
    }

    //创建路线信息表
    public void createTableRouteInfo(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_ROUTEINFO +
                "(RouteId varchar(60) primary key, " +
                "UserId varchar(60), " +
                "Point text, " +
                "RouteDate varchar(60), " +
                "RouteName varchar(60))");
    }

    //创建用户充值表
    public void createTableAddMoneyInfo(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_ADDMONEYINFO +
                "(AddId varchar(60) primary key, " +
                "UserId varchar(60), " +
                "AddDate varchar(60), " +
                "AddMoney varchar(60), " +
                "PayWay varchar(60))");
    }


    //------------------------------初始化数据--------------------------------------------------//
//    public void initData(){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Map<String, String> teachers_map = new HashMap<>();
//        //导入老师数据
//        for (int i=0;i<DataSourse.TEACHER.length;i++){
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("tch_name",DataSourse.TEACHER[i]);
//            contentValues.put("tch_number","2000"+i);
//            contentValues.put("age",new Random().nextInt(40));
//            contentValues.put("sex",new Random().nextInt(1));
//            String college_name =
//                    DataSourse.COLLEGE[new Random().nextInt(DataSourse.COLLEGE.length-1)];
//            contentValues.put("college_name",college_name);
//            contentValues.put("time", System.currentTimeMillis());
//            db.insert(TAB_TEACHER,null,contentValues);
//            teachers_map.put(DataSourse.TEACHER[i],college_name);
//            //确保每个学生数据都有导师
//            if (i<DataSourse.STUDENT.length){
//                ContentValues contentValues1 = new ContentValues();
//                contentValues1.put("stu_name",DataSourse.STUDENT[i]);
//                contentValues1.put("stu_number","1000"+i);
//                contentValues1.put("age",new Random().nextInt(30));
//                contentValues1.put("sex",new Random().nextInt(1));
//                contentValues1.put("college_name",college_name);
//                contentValues1.put("marjor_name",DataSourse.MAJOR[new Random().nextInt(DataSourse.MAJOR.length-1)]);
//                contentValues1.put("time", System.currentTimeMillis());
//                db.insert(TAB_STUDENT,null,contentValues1);
//
//
//            }
//        }
//        //导入学院数据
//        for (int i=0;i<DataSourse.COLLEGE.length;i++){
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("college_name",DataSourse.COLLEGE[i]);
//            db.insert(TAB_COLLEGE,null,contentValues);
//        }
//
//        //导入课程数据
//        for (int i =0;i<DataSourse.COURSE.length;i++){
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("course_name",DataSourse.COURSE[i]);
//            contentValues.put("course_credit",new Random().nextInt(10));
//            contentValues.put("course_hour",new Random().nextInt(20));
//            contentValues.put("course_time",DataSourse.TIMES[new Random().nextInt(DataSourse.TIMES.length-1)]);
//            contentValues.put("ach_point",0.5f+"");
//            contentValues.put("place",DataSourse.PLACES[new Random().nextInt(DataSourse.PLACES.length-1)]);
//            //确保每个老师都有一个课程，方便演示
//            String tch_name = DataSourse.TEACHER[new Random().nextInt(DataSourse.TEACHER.length-1)];
//            if (i<DataSourse.TEACHER.length)
//            {
//                tch_name = DataSourse.TEACHER[i];
//                if (i==0)
//                {
//                    //只给admin学生导入部分课程分数
//                    ContentValues values = new ContentValues();
//                    values.put("tch_name",tch_name);
//                    values.put("course_name",contentValues.getAsString("course_name"));
//                    values.put("score",new Random().nextInt(100));
//                    values.put("stu_number","10000");
//                    values.put("year",DataSourse.YEARS[new Random().nextInt(DataSourse.YEARS.length-1)]);
//                    db.insert(TAB_SCORE,null,values);
//                }
//
//            }
//            String college_name = teachers_map.get(tch_name);
//            contentValues.put("tch_name",tch_name);
//            contentValues.put("college_name",college_name);
//
//            db.insert(TAB_COURSE,null,contentValues);
//
//
//        }
//
//
//
//        db.close();
//    }
}
