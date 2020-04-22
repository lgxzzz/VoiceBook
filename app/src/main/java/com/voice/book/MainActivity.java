package com.voice.book;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.voice.book.bean.Budget;
import com.voice.book.bean.User;
import com.voice.book.data.DBManger;
import com.voice.book.fragment.AddFragment;
import com.voice.book.fragment.ChartFragment;
import com.voice.book.fragment.DetaildFragment;
import com.voice.book.util.ExcelUtil;
import com.voice.book.util.FragmentUtils;
import com.voice.book.util.HeadImgUtil;
import com.voice.book.util.SharedPreferenceUtil;
import com.voice.book.view.MenuView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivtiy {
    private Button mMenuBtn;
    private BottomNavigationView mBottomMenu;
    private MenuView mMenuView;
    private ImageView mHeadBtn;
    private Uri mCutUri;
    PopupWindow popupWindow;
    public static MainActivity _this;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        init();
        _this = this;

    }

    public void init(){
        mMenuBtn = findViewById(R.id.menu_btn);
        mBottomMenu = findViewById(R.id.bottom_menu);
        mBottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showFragment(item.getItemId());
                return true;
            }
        });
        mMenuView = new MenuView(this);

        mBottomMenu.setSelectedItemId(R.id.bottom_menu_garage);
        mMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow
                popupWindow.showAsDropDown(mMenuBtn);//设置popupWindow显示,并且告诉它显示在那个View下面
                //五秒后关闭弹窗
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(HSG_CLOSE_MENU,5000);
            }
        });
        popupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);//参数为1.View 2.宽度 3.高度
        mHeadBtn = findViewById(R.id.title_head_btn);
        mUser = DBManger.getInstance(this).mUser;
        //获取保存的头像
        Bitmap bitmap =  DBManger.getInstance(getApplicationContext()).getHeadImg();
        if (bitmap!=null){
            mHeadBtn.setImageBitmap(bitmap);
        }
    }


    /**
     * 根据id显示相应的页面
     * @param menu_id
     */
    private void showFragment(int menu_id) {
        switch (menu_id){
            case R.id.bottom_menu_garage:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, DetaildFragment.getInstance(),R.id.main_frame);
                break;
            case R.id.bottom_menu_fast:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, AddFragment.getInstance(),R.id.main_frame);
                break;
            case R.id.bottom_menu_navi:
                FragmentUtils.replaceFragmentToActivity(fragmentManager, ChartFragment.getInstance(),R.id.main_frame);
                break;
        }
    }


    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
                }

                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: //从相册图片后返回的uri
                    //启动裁剪
                    startActivityForResult(CutForPhoto(data.getData()),2);
                    break;
                case 2:
                    try {
                        //获取裁剪后的图片，并显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(mCutUri));
                        mHeadBtn.setImageBitmap(bitmap);
                        //保存头像
                        DBManger.getInstance(getApplicationContext()).saveHeadImg(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 图片裁剪
     * @param uri
     * @return
     */
    @NonNull
    private Intent CutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
            File cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    "cutcamera.png"); //随便命名一个
            if (cutfile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", 200); //200dp
            intent.putExtra("outputY",200);
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void exportExcel(){
        String filePath = Environment.getExternalStorageDirectory() + "/AndroidExcelDemo/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }


        String excelFileName = mUser.getUserName()+".xls";


        String[] title = {"类型", "数目", "时间"};
        String sheetName = "demoSheetName";


        filePath = filePath + excelFileName;
        List<Budget> mAllBudgets = DBManger.getInstance(this).getAllBudgetData();

        ExcelUtil.initExcel(filePath, title);


        ExcelUtil.writeObjListToExcel(mAllBudgets, filePath, getBaseContext());
    }

    public static final int HSG_CLOSE_MENU = 0;

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case HSG_CLOSE_MENU:
                    popupWindow.dismiss();
                    break;
            }
            return false;
        }
    });
}
