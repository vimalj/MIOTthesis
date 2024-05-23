package com.example.doctorhealthwatcher.util;

import android.content.Context;

import com.example.doctorhealthwatcher.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class MPUtil {

    private static int textSize = 10;

    public static ArrayList<BarDataSet> getDataSet(Context context, List<String> dataList) {

        ArrayList<BarDataSet> dataSets = new ArrayList<>();

        ArrayList<BarEntry> valueSet = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            valueSet.add(new BarEntry(Float.parseFloat((dataList.get(i))), i));
        }
        int[] colors = {context.getResources().getColor(R.color.color1),
                context.getResources().getColor(R.color.color2),
                context.getResources().getColor(R.color.color3),
                context.getResources().getColor(R.color.color4)};

        BarDataSet barDataSet = new MyBarDataSet(valueSet, "");
        barDataSet.setColors(colors);
        barDataSet.setValueTextSize(textSize);
        barDataSet.setBarSpacePercent(30);

        dataSets.add(barDataSet);

        return dataSets;
    }

    public static ArrayList<String> getXAxisValues(int dataSize) {

        ArrayList<String> xAxis = new ArrayList<>();

        for (int i = 0; i < dataSize; i++) {
            xAxis.add(String.valueOf(i + 1));
        }
        return xAxis;
    }


    public static void drawChart(final Context context, BarChart chart, BarData data) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(2);  // x ashix line size
        xAxis.setAxisLineColor(context.getResources().getColor(android.R.color.white));
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(textSize);
        xAxis.setTextColor(context.getResources().getColor(android.R.color.white));

        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setTextSize(textSize);
        yAxis.setTextColor(context.getResources().getColor(android.R.color.white));
        yAxis.setAxisLineColor(context.getResources().getColor(android.R.color.white));
        yAxis.setAxisLineWidth(2);  // y ashix line size

        data.setValueTextColor(context.getResources().getColor(android.R.color.white)); // bar data level
        data.setValueTextSize(12); // bar data size

        // bar data size

        chart.setData(data);
        chart.setDescription("");
        chart.invalidate();
        chart.setHorizontalScrollBarEnabled(true);
        chart.setVisibleXRange(8);
        chart.getLegend().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDoubleTapToZoomEnabled(false);
    }

}
