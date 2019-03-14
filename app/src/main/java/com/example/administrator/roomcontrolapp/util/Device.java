package com.example.administrator.roomcontrolapp.util;

import com.example.administrator.roomcontrolapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Device {
    private int deviceImageId;
    private String deviceName;
    private int serverId;
    private String value;
    private int deviceType;
    private int controllerId;
    private int deviceId;

    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }

    public void setDeviceType(int deviceType){
        this.deviceType = deviceType;
    }
    public int getDeviceType(){
        return deviceType;
    }

    public void setServerId(int serverId){
        this.serverId = serverId;
    }
    public int getServerId(){
        return serverId;
    }

    public void setDeviceId(int serverId){
        this.serverId = serverId;
    }
    public int getDeviceId(){
        return serverId;
    }
    public void setControllerId(int controllerId){
        this.controllerId = controllerId;
    }
    public int getControllerId(){
        return controllerId;
    }

    public ArrayList getDeviceStringList(){
        ArrayList<String> deviceStringList = new ArrayList<>();
        String deviceString;
        if (deviceType == 1){
//            String str[] = value.split(",");
            deviceStringList.add("t");
            deviceStringList.add("h");


//            Double cny = Double.parseDouble(str[0]);//转换成Double
//
//
//
//            if (cny <= 14){
//                holder.adviseText.setText("室内温度过低");
//            }

        }else if(deviceType == 2){
            deviceStringList.add("ill");

        }else if(deviceType == 3){
            deviceStringList.add("co2");

        }else if(deviceType == 4){
            deviceStringList.add("voc");

        }else if(deviceType == 5){
            deviceStringList.add("hcho");

        }else if(deviceType == 6){
            deviceStringList.add("pm1");
            deviceStringList.add("pm10");
            deviceStringList.add("pm25");

        }
        return deviceStringList;
    }

    public int getDeviceImageId(){
        if (deviceType >= 100 && deviceType <= 109) {
            if (value.equals("0"))
                deviceImageId = R.drawable.dswitch1;
            else
                deviceImageId = R.drawable.dswitch2;
        }

        switch (deviceType) {
            case 1:
                deviceImageId = R.drawable.dh2;

                break;
            case 2:
                deviceImageId = R.drawable.dill2;
                break;
            case 3:
                deviceImageId = R.drawable.dco22;
                break;
            case 4:
                deviceImageId = R.drawable.dvoc;
                break;
            case 5:
                deviceImageId = R.drawable.dhcho2;
                break;
            case 6:
                deviceImageId = R.drawable.dpm2;
                break;
            case 7:
                if (value.equals("0"))
                    deviceImageId = R.drawable.dswitch1;
                else
                    deviceImageId = R.drawable.dswitch2;
                break;
            case 9:
                if (value.equals("0")) {
                    deviceImageId = R.drawable.dsocket21;
                } else {
                    deviceImageId = R.drawable.dsocket22;
                }
                break;
            case 10:
                if (value.equals("0")) {
                    deviceImageId = R.drawable.dsocket31;
                } else {
                    deviceImageId = R.drawable.dsocket32;
                }
                break;
            case 11:
                String str[] = value.split(",");
                if (str[0].equals("0"))
                    deviceImageId = R.drawable.aircondition;
                else
                    deviceImageId = R.drawable.airconditioner_open;
                break;
            case 12:
                deviceImageId = R.drawable.curtain;
                break;
            case 128:
            case 129:
                if (value.equals("0"))
                    deviceImageId = R.drawable.dswitch1;
                else
                    deviceImageId = R.drawable.dswitch2;
                break;
            default:
                break;
        }
        return deviceImageId;

    }

    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }
    public String getDeviceName(){
        return deviceName;
    }

}
