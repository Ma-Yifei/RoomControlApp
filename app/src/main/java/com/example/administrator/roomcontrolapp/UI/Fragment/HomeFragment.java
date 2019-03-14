package com.example.administrator.roomcontrolapp.UI.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.administrator.roomcontrolapp.Adapter.ParameterDataAdapter;
import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.UI.LoginActivity;
import com.example.administrator.roomcontrolapp.UI.MainActivity;
import com.example.administrator.roomcontrolapp.UI.Menu.ChangeUserActivity;
import com.example.administrator.roomcontrolapp.UI.Menu.MonitorActivity;
import com.example.administrator.roomcontrolapp.util.Device;
import com.example.administrator.roomcontrolapp.util.ParameterData;
import com.example.administrator.roomcontrolapp.util.Url;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private String userName;
    private List<ParameterData> parameterDataList = new ArrayList<>();
    private List<Device> deviceList =new ArrayList<>();
    private Device device = new Device();
    private View view;
    private ParameterDataAdapter parameterDataAdapter;
    private int deviceType;
    private String deviceName;
    private double value;
    Timer timer = new Timer();
    private Intent intent;
    private String tem;
    private String location;
    private String weatherState;
    private String airQuality;
    private String wind;

    private TextView temView;
    private TextView locationView;
    private TextView weatherStateView;
    private TextView airQualityView;
    private TextView windView;
    private RefreshLayout mRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);

        temView = view.findViewById(R.id.tem);
        locationView = view.findViewById(R.id.location);
        weatherStateView = view.findViewById(R.id.weather_state);
        airQualityView = view.findViewById(R.id.air_quality);
        windView = view.findViewById(R.id.wind);

//        cond_txtView.setText(cond_txt);
        request();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // 需要做的事
                getWeather();

            }
        };
        //启动定时器
        timer.schedule(task, 0, 10*60*1000);
        //下拉刷新传感器参数数据
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                request();
                mRefreshLayout.finishRefresh();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.home_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.applyControl:
                Toast.makeText(getActivity(), "申请控制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancelControl:
                Toast.makeText(getActivity(), "注销控制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.viewMonitor:
                Toast.makeText(getActivity(), "查看监控", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), MonitorActivity.class);
                startActivity(intent);
                break;
            case R.id.updateVersion:
                Toast.makeText(getActivity(), "版本更新", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changeUser:
                Toast.makeText(getActivity(), "切换用户", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), ChangeUserActivity.class);
                startActivity(intent);
                break;
            case R.id.quit:
                Toast.makeText(getActivity(), "退出登录", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("count",MODE_PRIVATE);
                sharedPreferences.edit().putInt("count",0);
                getActivity().finish();
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

        }
        return true;
    }
    private void request(){
        //先获取本次登录账户的用户名
        MainActivity mainActivity = (MainActivity) getActivity();
        userName = mainActivity.getUserName();
        AndroidNetworking.initialize(getActivity().getApplicationContext());
        AndroidNetworking.get(Url.url+"GetDeviceServlet")
                .addQueryParameter("username",userName)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray deviceJSONArray = response.getJSONArray("devicedata");
                            deviceList.clear();
                            for (int i = 0; i < deviceJSONArray.length(); i++){
                                JSONObject deviceData = deviceJSONArray.getJSONObject(i);
                                device = new Device();
                                deviceType = deviceData.getInt("devicetype");
                                deviceName = deviceData.getString("devicename");
                                  if (1 <= deviceType && deviceType<= 6){
                                    device.setDeviceType(deviceType);
                                    device.setDeviceName(deviceName);
                                    deviceList.add(device);
                                }
                            }
                            AndroidNetworking.get(Url.url+"UserPrivilegeServlet")
                                    .addQueryParameter("username",userName)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                parameterDataList.clear();
                                                ArrayList nameList;
                                                for (int i = 0;deviceList.size() > i;i++) {
                                                    nameList = deviceList.get(i).getDeviceStringList();
                                                    for (int j = 0;nameList.size()>j;j++){
                                                        value = response.getDouble((String) nameList.get(j));
                                                        ParameterData parameterData = new ParameterData();
                                                        parameterData.setValue(value);
                                                        parameterData.setDeviceString((String) nameList.get(j));
                                                        parameterDataList.add(parameterData);
                                                    }
                                                }
//                                                parameterDataAdapter.notifyDataSetChanged();//提示主线程刷新界面
                                                parameterDataAdapter = new ParameterDataAdapter(getActivity(),parameterDataList);
                                                RecyclerView recyclerView = view.findViewById(R.id.parameter);

                                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setAdapter(parameterDataAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onError(ANError anError) {
                                        }
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                     }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    //获取当前本地天气信息--使用的和风天气API
    private void getWeather(){
        AndroidNetworking.initialize(getActivity().getApplicationContext());
        AndroidNetworking.get(Url.weatherURL)
                .addQueryParameter("location","auto_ip")
                .addQueryParameter("key",Url.weatherKey)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weather = response.getJSONArray("HeWeather6");
                            JSONObject nowWeather1 = weather.getJSONObject(0);
                            JSONObject nowWeather2 = nowWeather1.getJSONObject("now");
                            tem = nowWeather2.getString("tmp");
//                            location = nowWeather2.getString("parent_city");
                            weatherState = nowWeather2.getString("cond_txt");
//                            airQuality = nowWeather2.getString("tmp");
                            wind = nowWeather2.getString("wind_dir");
                            temView.setText(tem);
//                            locationView.setText(location);
                            weatherStateView.setText(weatherState);
                            windView.setText(wind);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

}
