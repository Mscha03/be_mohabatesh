package com.example.myapplication.chartadapter;

import android.content.Context;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.example.myapplication.R;
import com.example.myapplication.database.GetDates;
import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.model.HistoryModel;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class HistroyChartAdapter {
    private static final String TAG = "History";
     List<PieEntry> entries;
    static RoutineDB db;

    public PieData pieChartEntry(Context context, int id) {
        db = new RoutineDB(context);
        entries = new ArrayList<>();
        Log.d(TAG, "pieChartEntry: database initialized");

        GetDates getDates = new GetDates();
        List<HistoryModel> data = getDates.getTaskHistoryList(context, id);
        List<Integer> chartData = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            chartData.add(0);
        }

        for (int i = 0; i < data.size(); i++) {
            int j = data.get(i).getIsDone();
            chartData.set(j, (chartData.get(j) + 1));
        }

        entries.add(new PieEntry(chartData.get(0), context.getString(R.string.history_chart_undone)));
        entries.add(new PieEntry(chartData.get(1), context.getString(R.string.history_chart_green_done)));
        entries.add(new PieEntry(chartData.get(2), context.getString(R.string.history_chart_yellow_done)));
        entries.add(new PieEntry(chartData.get(3), context.getString(R.string.history_chart_red_done)));

        PieDataSet pieDataSet = new PieDataSet(entries, " ");
        pieDataSet.setColor(ContextCompat.getColor(context, R.color.gray));
        pieDataSet.addColor(ContextCompat.getColor(context, R.color.green));
        pieDataSet.addColor(ContextCompat.getColor(context, R.color.yellow));
        pieDataSet.addColor(ContextCompat.getColor(context, R.color.red));


        PieData pieData = new PieData(pieDataSet);

        Log.d(TAG, "pieChartEntry: entries list created");
        return pieData;
    }



}
