package com.example.administrator.roomcontrolapp.Tools;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
//向服务器发送对设备的控制指令
public class ControlData {
    public static boolean push(final int controllerId, final Object data) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Socket socket = new Socket("182.254.136.237", 2597);
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();
                    JSONObject jo = new JSONObject();
                    jo.put("controllerid", controllerId);
                    jo.put("data", data);
                    // String data = "{\"controllerid\":1208399865, \"data\":{\"deviceid\":135, \"devicetype\":7, \"value\":\"1\"}}";
                    String str = jo.toString();
                    byte[] buffer = str.getBytes(Charset.forName("utf-8"));
                    out.write(intToByte(buffer.length));
                    out.write(buffer);
                    out.flush();
                    out.close();
                    in.close();
                    socket.close();
                } catch (Exception e) {
                }
            }

        }).start();

        return true;
    }

    public static byte[] intToByte(int i) {
        byte[] abyte0 = new byte[4];
        abyte0[0] = (byte) (0xff & i);
        abyte0[1] = (byte) ((0xff00 & i) >> 8);
        abyte0[2] = (byte) ((0xff0000 & i) >> 16);
        abyte0[3] = (byte) ((0xff000000 & i) >> 24);
        return abyte0;
    }
    public static void controlDevice(int controllerid, int deviceid, int devicetype, String value) {
        String js = "{\"deviceid\":" + deviceid + ",\"devicetype\":" + devicetype + ",\"value\":\"" + value + "\",\"func\":\"control\"}";
        push(controllerid,js);
    }
}
