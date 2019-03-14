package com.example.administrator.roomcontrolapp.UI.Menu;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.roomcontrolapp.Adapter.DeviceRecyclerAdapter;
import com.example.administrator.roomcontrolapp.Adapter.ParameterDataAdapter;
import com.example.administrator.roomcontrolapp.Adapter.UserRecyclerAdapter;
import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.util.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChangeUserActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private UserRecyclerAdapter userRecyclerAdapter;
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        SharedPreferences sp = getSharedPreferences("users",MODE_PRIVATE);
        String json = sp.getString("user","");
        Type type = new TypeToken<List<User>>(){}.getType();
        Gson gson = new Gson();
        users = gson.fromJson(json, type);
        initViews();

    }
    //获取界面
    private void initViews() {
//        request();
        //获得布局的RecyclerView控件
        mRecyclerView = (RecyclerView)findViewById(R.id.users);
        //设置布局显示方式，这里使用LinearLayoutManager
        userRecyclerAdapter = new UserRecyclerAdapter(this,users);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(userRecyclerAdapter);
    }
}
