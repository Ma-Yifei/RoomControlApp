package com.example.administrator.roomcontrolapp.UI.Fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.administrator.roomcontrolapp.Adapter.DeviceRecyclerAdapter;
import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.UI.MainActivity;
import com.example.administrator.roomcontrolapp.util.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private DeviceRecyclerAdapter mRecyclerAdapter;
    private List<Device> deviceList = new ArrayList<>();
    private Device device;
    private View view;
    Timer timer = new Timer();
    private String userName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.device_fragment,container,false);
        request();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // 需要做的事
                request();
            }
        };
        //启动定时器

        timer.schedule(task, 2500, 2500);
        initViews();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.device_menu,menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_device:
                Toast.makeText(getActivity(), "添加设备", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }



    //向服务器获取数据
    private void request(){
        //先获取本次登录账户的用户名
        MainActivity mainActivity = (MainActivity) getActivity();
        userName = mainActivity.getUserName();

        AndroidNetworking.initialize(getActivity().getApplicationContext());
        AndroidNetworking.get("http://182.254.136.237:8080/RoomServer/GetDeviceServlet")
                .addQueryParameter("username",userName)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray deviceJSONArray = response.getJSONArray("devicedata");
                            deviceList.clear();
                            for (int i = 0; i < deviceJSONArray.length(); i++){
                                JSONObject deviceData = deviceJSONArray.getJSONObject(i);
                                device = new Device();

                                device.setControllerId(deviceData.getInt("controllerid"));
                                device.setDeviceName(deviceData.getString("devicename"));
                                device.setDeviceType(deviceData.getInt("devicetype"));
                                device.setServerId(deviceData.getInt("serverid"));
                                device.setValue(deviceData.getString("value"));
                                device.setDeviceId(deviceData.getInt("deviceid"));
                                deviceList.add(device);

//                                //在设备界面可见时才可
//                                if (isVisible()){
//                                    Toast.makeText(getContext(), " "+devicename, Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    break;
//                                }

                            }

                            mRecyclerAdapter.notifyDataSetChanged();//提示主线程刷新界面

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    //获取界面
    private void initViews() {
//        request();
        //获得布局的RecyclerView控件
        mRecyclerView = (RecyclerView)view.findViewById(R.id.devices);
        //设置布局显示方式，这里使用GridLayoutManager
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        //设置添加删除Item的时候的动画效果
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //初始化适配器
        mRecyclerAdapter = new DeviceRecyclerAdapter(this.getActivity(),deviceList);
        //设置适配器
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }


}
