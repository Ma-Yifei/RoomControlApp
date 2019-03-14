package com.example.administrator.roomcontrolapp.Adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.util.ParameterData;

import java.text.DecimalFormat;
import java.util.List;

public class ParameterDataAdapter extends RecyclerView.Adapter<ParameterDataAdapter.ViewHolder> {

    private Context context;
    private List<ParameterData> parameterDataList;
    private View view;
    public ParameterDataAdapter(FragmentActivity context,List<ParameterData> parameterDataList){
        this.context = context;
        this.parameterDataList = parameterDataList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parameter_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ParameterData parameterData = parameterDataList.get(position);
        Double value = parameterData.getValue();
//        holder.dataText.setText(value+"℃");
//        判断是那种环境参数
        holder.adviseText.setText("");
        if (parameterData.getDeviceString().equals("t")){
//            String str[] = value.split(",");
            holder.dataText.setText(value+"℃");
            if (value < 15)
                holder.adviseText.setText("温度偏低，注意保暖并采取调控措施");
            else if (value > 25)
                holder.adviseText.setText("温度偏高，请注意室内通风以及采取响应的降温措施");
            else
                holder.adviseText.setText("温度适宜");
//            Double cny = Double.parseDouble(str[0]);//转换成Double
//
//
//
//            if (cny <= 14){
////                holder.adviseText.setText("室内温度过低");
////            }

        }else if(parameterData.getDeviceString().equals("h")){
            holder.dataText.setText(value+"%");
            if (value < 40)
                holder.adviseText.append("空气湿度较低，容易使人感觉不适~");
            else if (value < 80)
                holder.adviseText.append("空气湿度适宜");
            else
                holder.adviseText.append("空气湿度较高");
        }else if(parameterData.getDeviceString().equals("ill")){
            holder.dataText.setText(value+"lux");
            if (value < 50)
                holder.adviseText.append("光照度极低，爱护眼睛，打开照明");
            else if (value < 80)
                holder.adviseText.append("光照度偏低，请及时打开照明");
            else if (value < 200)
                holder.adviseText.append("室内光照度适宜");
            else
                holder.adviseText.append("光照度偏高！爱护环境，从关灯开始");
        }else if(parameterData.getDeviceString().equals("co2")){
            holder.dataText.setText(value+"ppm");
            if (value < 350)
                holder.adviseText.setText("二氧化碳浓度太低了~你确定生活在地球上？");
            else if (value < 1000)
                holder.adviseText.setText("空气清新，呼吸顺畅");
            else if (value < 2000)
                holder.adviseText.setText("空气浑浊，并开始觉得昏昏欲睡，适当通通风吧");
            else if (value < 5000)
                holder.adviseText.setText("二氧化碳浓度非常高，请及时通风！否则会出现头痛、嗜睡、呆滞、注意力无法集中、心跳加速、轻度恶心等症状");
            else
                holder.adviseText.setText("警告，二氧化碳浓度超高，可能导致严重缺氧，造成永久性脑损伤、昏迷、甚至死亡，请开窗通风，及时打开空调！！！");

        }else if(parameterData.getDeviceString().equals("voc")){
            holder.dataText.setText(value+"%");
        }else if(parameterData.getDeviceString().equals("hcho")){
            holder.dataText.setText(value+"ppb");
            if (value < 40)
                holder.adviseText.append("空气清新，甲醛含量很低~");
            else if (value < 80)
                holder.adviseText.append("空气清新，甲醛含量符合国家标准");
            else if (value < 120)
                holder.adviseText.append("空气中弥漫着甲醛的味道，请开窗通风");
            else if (value < 200)
                holder.adviseText.append("甲醛含量超标！");
            else
                holder.adviseText.append("甲醛含量严重超标，请及时采取有效措施");
        }else if(parameterData.getDeviceString().equals("pm1")){
            holder.dataText.setText(value+"ug/m3");
            if (value < 40)
                holder.adviseText.append("空气清新，可吸入微粒含量极低~");
            else if (value < 80)
                holder.adviseText.append("空气浑浊，空气中弥漫着可吸入颗粒物");
            else if (value < 120)
                holder.adviseText.append("PM含量偏高");
            else if (value < 200)
                holder.adviseText.append("PM含量超高，请及时采取有效措施");
            else
                holder.adviseText.append("PM含量已达到危险值，请及时开窗通风，开启换气设施");
        }else if(parameterData.getDeviceString().equals("pm10")){
            holder.dataText.setText(value+"ug/m3");
            if (value < 40)
                holder.adviseText.append("空气清新，可吸入微粒含量极低~");
            else if (value < 80)
                holder.adviseText.append("空气浑浊，空气中弥漫着可吸入颗粒物");
            else if (value < 120)
                holder.adviseText.append("PM含量偏高");
            else if (value < 200)
                holder.adviseText.append("PM含量超高，请及时采取有效措施");
            else
                holder.adviseText.append("PM含量已达到危险值，请及时开窗通风，开启换气设施");
        }else if(parameterData.getDeviceString().equals("pm25")){
            holder.dataText.setText(value+"ug/m3");
            if (value < 40)
                holder.adviseText.append("空气清新，可吸入微粒含量极低~");
            else if (value < 80)
                holder.adviseText.append("空气浑浊，空气中弥漫着可吸入颗粒物");
            else if (value < 120)
                holder.adviseText.append("PM含量偏高");
            else if (value < 200)
                holder.adviseText.append("PM含量超高，请及时采取有效措施");
            else
                holder.adviseText.append("PM含量已达到危险值，请及时开窗通风，开启换气设施");
        }

    }

    @Override
    public int getItemCount() {
        return parameterDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dataText;
        TextView adviseText;
        public ViewHolder(View itemView) {
            super(itemView);
            dataText = itemView.findViewById(R.id.data);
            adviseText = itemView.findViewById(R.id.advise);
        }
    }


}

