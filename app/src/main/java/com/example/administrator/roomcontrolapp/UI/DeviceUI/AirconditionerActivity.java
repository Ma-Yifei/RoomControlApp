package com.example.administrator.roomcontrolapp.UI.DeviceUI;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.Tools.ControlData;
import com.example.administrator.roomcontrolapp.util.Device;
import com.example.administrator.roomcontrolapp.util.Url;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class AirconditionerActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView windSpeed;
    private ImageView windSpeedImage;
    private TextView windSpeedText;
    private ImageView switchImage;
    private RelativeLayout switchLayout;
    private TextView modal;
    private ImageView modelImage;
    private TextView modelText;
    private RelativeLayout decreaseLayout;
    private TextView temperatureText;
    private RelativeLayout addLayout;
    private LinearLayout showTemperature;

    private TextView windspeedText;
    private TextView centigrade;

    private int windSpeedCount = 0;
    private int switchCount = 0;
    private int modelCount = 0;
    int temperatureCount = 25;

    private String[] str;
    private String value;
    private int controlerid;
    private int deviceid;
    private int devicetype;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airconditioner);
        windSpeed = findViewById(R.id.wind_speed);
        windSpeed.setOnClickListener(this);


        windSpeedImage = findViewById(R.id.imageView);
        windSpeedText = findViewById(R.id.textView3);

        switchImage = findViewById(R.id.switch1);
        switchImage.setOnClickListener(this);
        switchLayout = findViewById(R.id.switch_view);
        switchLayout.setOnClickListener(this);

        modal = findViewById(R.id.model);
        modal.setOnClickListener(this);

        modelImage = findViewById(R.id.imageView2);
        modelText = findViewById(R.id.textView4);

        decreaseLayout = findViewById(R.id.decrease_view);
        decreaseLayout.setOnClickListener(this);
        temperatureText = findViewById(R.id.temperature);
        addLayout = findViewById(R.id.add_view);
        addLayout.setOnClickListener(this);

        windspeedText = findViewById(R.id.textView2);
        centigrade = findViewById(R.id.textView);

        showTemperature = findViewById(R.id.show);

//        modelImage.setVisibility(View.INVISIBLE);
        //先判断目前空调是否开启
        Intent intent = getIntent();
        value = intent.getStringExtra("value");
        controlerid = intent.getIntExtra("controllerid",0);
        deviceid = intent.getIntExtra("deviceid",0);
        devicetype = intent.getIntExtra("devicetype",0);
//        String value = ("1,26,1,1");
        str = value.split(",");//str[0]:开关、str[1]:温度、str[2]:风速、str[3]:模式


        //空调未开启
        if (str[0].equals("0")){
            showTemperature.setVisibility(View.INVISIBLE);//使界面上部分不可见，在点击开关按钮后在显示出来；
            switchImage.setImageResource(R.drawable.airconditioner_switch);
        }
        //空调已开启
        else if (str[0].equals("1")){
            switchImage.setImageResource(R.drawable.switch_red);
            temperatureText.setText(str[1]);
            if (str[2].equals("0")){
                windSpeedImage.setImageResource(R.drawable.wind_speed0);
                windSpeedText.setText("自动");
            }else if (str[2].equals("1")){
                windSpeedImage.setImageResource(R.drawable.wind_speed1);
                windSpeedText.setText("低");
            }else if (str[2].equals("2")){
                windSpeedImage.setImageResource(R.drawable.wind_speed2);
                windSpeedText.setText("中");
            }else if (str[2].equals("3")){
                windSpeedImage.setImageResource(R.drawable.wind_speed3);
                windSpeedText.setText("高");
            }
            if (str[3].equals("0")){
                modelImage.setImageResource(R.drawable.refrigeration);
                modelText.setText("制冷");
            }else if (str[3].equals("1")){
                modelImage.setImageResource(R.drawable.heating);
                modelText.setText("制热");
            }
        }

        //在顶部加一个返回键
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //点击顶部的返回键结束该界面
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //改变风速
            case R.id.wind_speed:
                //windSpeedCount为0，风速为自动；windSpeedCount为1，风速为一档；2——二档；3——三档
                if (windSpeedCount >= 3){
                    windSpeedCount = 0;

                }else {
                    windSpeedCount++;

                }
                if (windSpeedCount == 1){
                    windSpeedImage.setImageResource(R.drawable.wind_speed1);
                    windSpeedText.setText("低");
                    str[2] = "1";
                }else if (windSpeedCount == 2){
                    windSpeedImage.setImageResource(R.drawable.wind_speed2);
                    windSpeedText.setText("中");
                    str[2] = "2";
                }else if (windSpeedCount == 3){
                    windSpeedImage.setImageResource(R.drawable.wind_speed3);
                    windSpeedText.setText("高");
                    str[2] = "3";
                }else if (windSpeedCount == 0){
                    windSpeedImage.setImageResource(R.drawable.wind_speed0);
                    windSpeedText.setText("自动");
                    str[2] = "0";
                }
//                controlDevice(controlerid,deviceid,devicetype,str);
                break;
                //开关
            case R.id.switch_view : case R.id.switch1:
                if (switchCount == 1){
                    switchCount = 0;
                }else {
                    switchCount++;
                }
                if (switchCount == 0){


                    switchImage.setImageResource(R.drawable.airconditioner_switch);

                    showTemperature.setVisibility(View.INVISIBLE);//在开关关闭时界面上部分不可见
                    str[0] = "0";
                }else{
                    switchImage.setImageResource(R.drawable.switch_red);
                    showTemperature.setVisibility(View.VISIBLE);//在开关打开时界面上部分可见
                    str[0] = "1";
                }
//                controlDevice(controlerid,deviceid,devicetype,str);
                break;

                //模式——制冷制热
            case R.id.model:
                if (modelCount == 1){
                    modelCount = 0;
                }else {
                    modelCount++;
                }
                if (modelCount == 0){

                    modelImage.setImageResource(R.drawable.refrigeration);
                    modelText.setText("制冷");
                    str[3] = "0";
                }else{
                    modelImage.setImageResource(R.drawable.heating);
                    modelText.setText("制热");
                    str[3] = "1";
                }
//                controlDevice(controlerid,deviceid,devicetype,str);
                break;
                //降温
            case R.id.decrease_view:
               if (temperatureCount == 20){
                    temperatureCount = 28;
                }else if(temperatureCount == 25){
                    temperatureCount = 20;
                }else if(temperatureCount == 28) {
                   temperatureCount = 25;
               }
                String temperature1 = Integer.toString(temperatureCount);
                temperatureText.setText(temperature1);
                str[1] = String.valueOf(temperatureCount);
//                controlDevice(controlerid,deviceid,devicetype,str);
                break;
                //升温
            case R.id.add_view:
                if (temperatureCount == 20){
                    temperatureCount = 25;
                }else if(temperatureCount == 25) {
                    temperatureCount = 28;
                }else if(temperatureCount == 28) {
                    temperatureCount = 20;
                }
                String temperature2 = Integer.toString(temperatureCount);
                temperatureText.setText(temperature2);
                str[1] = String.valueOf(temperatureCount);
                String value = str[0];
//                controlDevice(controlerid,deviceid,devicetype,value);
                break;

        }
        String value = str[0]+","+str[1]+","+str[2]+","+str[3];
        ControlData.controlDevice(controlerid,deviceid,devicetype,value);
    }
//    public static boolean push(final int controllerId, final Object data) {
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    Socket socket = new Socket("182.254.136.237", 2597);
//                    InputStream in = socket.getInputStream();
//                    OutputStream out = socket.getOutputStream();
//                    JSONObject jo = new JSONObject();
//                    jo.put("controllerid", controllerId);
//                    jo.put("data", data);
//                    // String data = "{\"controllerid\":1208399865, \"data\":{\"deviceid\":135, \"devicetype\":7, \"value\":\"1\"}}";
//                    String str = jo.toString();
//                    byte[] buffer = str.getBytes(Charset.forName("utf-8"));
//                    out.write(intToByte(buffer.length));
//                    out.write(buffer);
//                    out.flush();
//                    out.close();
//                    in.close();
//                    socket.close();
//                } catch (Exception e) {
//                }
//            }
//
//        }).start();
//
//        return true;
//    }
//
//
//
//    public static byte[] intToByte(int i) {
//        byte[] abyte0 = new byte[4];
//        abyte0[0] = (byte) (0xff & i);
//        abyte0[1] = (byte) ((0xff00 & i) >> 8);
//        abyte0[2] = (byte) ((0xff0000 & i) >> 16);
//        abyte0[3] = (byte) ((0xff000000 & i) >> 24);
//        return abyte0;
//    }
//    public static void controlDevice(int controllerid, int deviceid, int devicetype, String[] str) {
//        String value = str[0]+","+str[1]+","+str[2]+","+str[3];
//        String js = "{\"deviceid\":" + deviceid + ",\"devicetype\":" + devicetype + ",\"value\":\"" + value + "\",\"func\":\"control\"}";
//        push(controllerid,js);
//    }
}
