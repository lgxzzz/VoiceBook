package com.voice.book;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.voice.book.fragment.AddFragment;
import com.voice.book.fragment.ChartFragment;
import com.voice.book.fragment.DetaildFragment;
import com.voice.book.util.FragmentUtils;


public class MainActivity extends BaseActivtiy {

    private BottomNavigationView mBottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }

    public void init(){
        mBottomMenu = findViewById(R.id.bottom_menu);
        mBottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showFragment(item.getItemId());
                return true;
            }
        });



        mBottomMenu.setSelectedItemId(R.id.bottom_menu_garage);

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
}
