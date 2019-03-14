package com.example.administrator.roomcontrolapp.util;

public class ParameterData {
    private int deviceId;
    private String deviceName;
    private int serverId;
    private Double value;
    private int deviceType;
    private int controllerId;

    private String deviceString;
    public void setDeviceString(String deviceString){
        this.deviceString = deviceString;
    }
    public String getDeviceString(){
        return deviceString;
    }
    public void setValue(double value){
        this.value = value;
    }
    public Double getValue(){
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

    public void setControllerId(int controllerId){
        this.controllerId = controllerId;
    }
    public int getControllerId(){
        return controllerId;
    }
    public int getDeviceId(){
        return deviceId;
    }
    public void setDeviceName(String deviceName){
        this.deviceName = deviceName;
    }
    public String getDeviceName(){
        return deviceName;
    }


}
