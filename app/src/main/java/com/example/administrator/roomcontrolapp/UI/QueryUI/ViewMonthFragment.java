package com.example.administrator.roomcontrolapp.UI.QueryUI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.UI.MainActivity;
import com.example.administrator.roomcontrolapp.util.Url;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewMonthFragment extends Fragment {
    private PieChart picChart;
    private LineChart lineChart;

    private ArrayList<String> xValues = new ArrayList<String>();
    private ArrayList<Entry> Values = new ArrayList<Entry>();
    private XAxis xAxis;//X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_month,container,false);


        picChart = view.findViewById(R.id.pic_chart);
        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(30f,"插座"));
        strings.add(new PieEntry(40f,"开关"));
        strings.add(new PieEntry(30f,"空调插座"));
        PieDataSet dataSet = new PieDataSet(strings,"Label");
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.login_red));
        colors.add(getResources().getColor(R.color.blue));
        colors.add(getResources().getColor(R.color.green));
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        picChart.setData(pieData);
        picChart.invalidate();
        lineChart = view.findViewById(R.id.linechart);
        initChart(lineChart);
        showLineChart("总用电量", Color.CYAN);
        return view;
    }

    /**

     * 初始化图表

     */
    private void initChart(final LineChart lineChart) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);    //是否显示边界
        lineChart.setDrawBorders(true);    //是否可以拖动
        lineChart.setDragEnabled(true);    //是否有触摸事件
        lineChart.setTouchEnabled(true);    //设置XY轴动画效果

        lineChart.animateY(2500);
        lineChart.animateX(1500);
        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(xValues.get((int) value));
            }
        });
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);
        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);    //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }
    /**

     * 曲线初始化设置 一个LineDataSet 代表一条曲线

     *

     * @param lineDataSet 线条

     * @param color       线条颜色

     * @param mode

     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }
    //获取一周内的用电量
    public void showLineChart(final String name, final int color) {
        MainActivity mainActivity = (MainActivity) getActivity();
        String userName = mainActivity.getUserName();
        AndroidNetworking.initialize(getActivity().getApplicationContext());
        AndroidNetworking.get(Url.url+"GetControllerEnergy")
                .addQueryParameter("username",userName)
                .addQueryParameter("time","month")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weekDataJSONArray = response.getJSONArray("data");
                            xValues.clear();
                            Values.clear();
                            for (int i = 0;i<weekDataJSONArray.length();i++){
                                JSONObject dayData = weekDataJSONArray.getJSONObject(i);
                                xValues.add(dayData.getString("time"));
//                                Log.d("ViewMonthFragment",dayData.getString("time"));
                                Values.add(new Entry(i,(float) dayData.getDouble("power")));
//                                Log.d("ViewMonthFragment",""+ new Entry(i,(float) dayData.getDouble("power")));
                            }
//                            lineChart.notifyDataSetChanged();//等收到数据后提示view更新界面
                            LineDataSet lineDataSet = new LineDataSet(Values, name);
                            initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
                            LineData lineData = new LineData(lineDataSet);
                            lineChart.setData(lineData);
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
