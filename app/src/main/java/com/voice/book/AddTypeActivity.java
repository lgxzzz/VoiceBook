package com.voice.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.voice.book.bean.User;
import com.voice.book.data.DBManger;

import java.util.List;


public class AddTypeActivity extends AppCompatActivity {

    ListView mIncomeListView;
    ListView mExpenseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        init();
    }

    public void init(){

    }
}
