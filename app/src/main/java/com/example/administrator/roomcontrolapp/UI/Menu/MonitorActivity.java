package com.example.administrator.roomcontrolapp.UI.Menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.roomcontrolapp.R;
import com.ezvizuikit.open.EZUIKit;
import com.ezvizuikit.open.EZUIPlayer;
//获取EZUIPlayer实例
public class MonitorActivity extends AppCompatActivity {
    private EZUIPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        mPlayer = (EZUIPlayer) findViewById(R.id.player_ui);

    }
//    public static EZUIPlayer.EZUIKitPlayMode getUrlPlayType(String url){
//
//    }

}
