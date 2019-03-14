package com.example.administrator.roomcontrolapp.UI;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.UI.Fragment.DeviceFragment;
import com.example.administrator.roomcontrolapp.UI.Fragment.HomeFragment;
import com.example.administrator.roomcontrolapp.UI.Fragment.ModelFragment;
import com.example.administrator.roomcontrolapp.UI.Fragment.QueryFragment;
import com.example.administrator.roomcontrolapp.UI.Fragment.StrategyFragment;
import com.example.administrator.roomcontrolapp.util.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    HomeFragment homeFragment ;
    DeviceFragment deviceFragment;
    ModelFragment modelFragment;
    QueryFragment queryFragment;
    StrategyFragment strategyFragment;
    private int count = 0;
    private String userName;
    Fragment currentFragment;
    FragmentManager fm = getSupportFragmentManager();
//    FragmentTransaction transaction = fm.beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_CLASSIC);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.home1, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.device1, "设备")).setActiveColor("#000000")
                .addItem(new BottomNavigationItem(R.drawable.mode1, "模式")).setActiveColor("#000000")
                .addItem(new BottomNavigationItem(R.drawable.method1, "策略")).setActiveColor("#000000")
                .addItem(new BottomNavigationItem(R.drawable.search1, "查询")).setActiveColor("#000000")
                .setFirstSelectedPosition(0)
                .initialise();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);
//        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        //判断之前该账户已登录成功，并未退出登录，此时将直接进入主界面。如之前没有登录，此次打开软件将首先进入登录界面
        SharedPreferences sp = getSharedPreferences("users",MODE_PRIVATE);
        String json1 = sp.getString("user",null);
        if (json1 == null || json1.equals("")){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.add(R.id.layFrame,homeFragment);
        currentFragment = homeFragment;
        transaction.commit();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position){
            case 0:
                if (homeFragment==null){
                    homeFragment = new HomeFragment();
                }showFragment(R.id.layFrame,homeFragment);
                break;
            case 1:
                if (deviceFragment==null){
                    deviceFragment = new DeviceFragment();
                }
                showFragment(R.id.layFrame,deviceFragment);
                break;
            case 2:
                if (modelFragment==null){
                    modelFragment = new ModelFragment();
                }showFragment(R.id.layFrame,modelFragment);
                break;
            case 3:
                if (strategyFragment==null){
                    strategyFragment = new StrategyFragment();
                }showFragment(R.id.layFrame,strategyFragment);
                break;
            case 4:
                if (queryFragment==null){
                    queryFragment = new QueryFragment();
                }showFragment(R.id.layFrame,queryFragment);
                 break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

   //将登录时存入SharedPreferences中的username取出，然后各个Fragment通过 MainActivity的实例来调用getUserName的方法来获取用户名。
    public String getUserName(){
        SharedPreferences sp = getSharedPreferences("users",MODE_PRIVATE);
        String json1 = sp.getString("user",null);
        Type type = new TypeToken<List<User>>(){}.getType();
        Gson gson1 = new Gson();
        ArrayList<User> users = new ArrayList<>();
        users = gson1.fromJson(json1, type);
        userName = users.get((users.size()-1)).getUsername();
        return userName;
    }

    private void showFragment(int id,Fragment fg){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//每提交（commit）一次transaction就要获取一次
        //如果之前没有添加过，先将上一个fragment隐藏，再add要显示的fragment
        if(!fg.isAdded()){
            transaction.hide(currentFragment).add(id,fg);
        }//如果之前已经添加过，将上一个fragment隐藏后直接show要显示的fragment
        else{
            transaction.hide(currentFragment).show(fg);
        }
        currentFragment = fg;
        transaction.commit();
    }
}
