package com.example.administrator.roomcontrolapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.Tools.ControlData;
import com.example.administrator.roomcontrolapp.UI.DeviceUI.AirconditionerActivity;
import com.example.administrator.roomcontrolapp.UI.Fragment.DeviceFragment;
import com.example.administrator.roomcontrolapp.UI.MainActivity;
import com.example.administrator.roomcontrolapp.util.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceRecyclerAdapter extends RecyclerView.Adapter<DeviceRecyclerAdapter.ViewHolder> {
    private List<Device> deviceList = new ArrayList<>();
    private View view;
    FragmentActivity context;

    public DeviceRecyclerAdapter(FragmentActivity context, List<Device> deviceList){
        this.deviceList = deviceList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item,parent,false);
       ViewHolder holder = new ViewHolder(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Device device = deviceList.get(position);
        holder.deviceImage.setImageResource(device.getDeviceImageId());
        holder.deviceName.setText(device.getDeviceName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Device device1 = deviceList.get(position);
                //点击空调图标
                if (device1.getDeviceType() == 11){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(context, AirconditionerActivity.class));
                    intent.putExtra("value",device1.getValue());
                    intent.putExtra("controllerid",device1.getControllerId());
                    intent.putExtra("deviceid",device1.getDeviceId());
                    intent.putExtra("devicetype",device1.getDeviceType());
                    context.startActivity(intent);
                }
                //点击光照度
                else if (device1.getDeviceType() == 2){
                    showExitDialog02();//出现对话框
                }
                //点击开关时
                else if (device1.getDeviceType() >= 100 && device1.getDeviceType() <= 128 ) {
                    if (device1.getValue().equals("0")){
                        device1.setValue("1");
                        holder.deviceImage.setImageResource(R.drawable.dswitch2);
                    }else{
                        device1.setValue("0");
                        holder.deviceImage.setImageResource(R.drawable.dswitch1);
                    }
                    ControlData.controlDevice(device1.getControllerId(),device1.getDeviceId(),device1.getDeviceType(),device1.getValue());
                }
                //点击插座图标
                else if (device1.getDeviceType() == 10){
                    if (device1.getValue().equals("0")){
                        device1.setValue("1");
                        holder.deviceImage.setImageResource(R.drawable.dsocket32);
                    }else{
                        device1.setValue("0");
                        holder.deviceImage.setImageResource(R.drawable.dsocket31);
                    }
                    ControlData.controlDevice(device1.getControllerId(),device1.getDeviceId(),device1.getDeviceType(),device1.getValue());
                }
                //点击空调插座
                else if (device1.getDeviceType() == 9){
                    if (device1.getValue().equals("0")){
                        device1.setValue("1");
                        holder.deviceImage.setImageResource(R.drawable.dsocket22);
                    }else{
                        device1.setValue("0");
                        holder.deviceImage.setImageResource(R.drawable.dsocket21);
                    }
                    ControlData.controlDevice(device1.getControllerId(),device1.getDeviceId(),device1.getDeviceType(),device1.getValue());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;
        ImageView deviceImage;
        public ViewHolder(View itemView) {
            super(itemView);
            deviceImage = itemView.findViewById(R.id.device_image);
            deviceName = itemView.findViewById(R.id.device_name);

        }
    }
    private void showExitDialog02(){
//        new AlertDialog.Builder(context)
//                .setTitle("光照度信息")
//                .setMessage("光照度：88 lux" +"\n"+"建 议：当前光照度适宜")
//                .setPositiveButton("确定", null)
//                .show();

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("光照度信息")
                .setMessage("光照度：88 lux" +"\n"+"建 议：当前光照度适宜")
                .setPositiveButton("确定", null).create();
        Window window = alertDialog.getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        WindowManager.LayoutParams lp = window.getAttributes(); // 设置透明度为0.3
        lp.alpha = 0.8f;
        lp.dimAmount=0.5f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
        alertDialog.show();////

    }

}
