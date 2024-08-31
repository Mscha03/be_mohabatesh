package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.bottomsheet.PeriodTaskBottomSheet;
import com.example.myapplication.chartadapter.HistroyChartAdapter;
import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.model.Period;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PeriodicTaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "PeriodicTaskDetailActivity";

    private ImageButton edit, delete;
    private TextView title, description, period;
    private RoutineDB db;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_periodic_task_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_delete_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initials
        edit = findViewById(R.id.edit_button_edit_activity);
        delete = findViewById(R.id.delete_button_edit_activity);
        title = findViewById(R.id.task_title_edit_activity);
        description = findViewById(R.id.task_description_edit_activity);
        period = findViewById(R.id.period_text_edit_activity);
        db = new RoutineDB(this);
        Log.d(TAG, "onCreate: views initialized");

        int id = getIntent().getIntExtra("task", 0);
        Log.d(TAG, "onCreate: received task ID: " + id);

        Cursor cursor = db.getRecord(id);
        String[] detail = new String[4];
        detail[0] = cursor.getString(1);
        detail[1] = cursor.getString(2);
        detail[2] = cursor.getString(3);
        detail[3] = String.valueOf(id);

        title.setText(detail[0]);
        description.setText(detail[1]);
        String period = "";
        if (detail[2].equals(Period.daily.toString())){
            period = getString(R.string.period_daily);
        }else if (detail[2].equals(Period.weekly.toString())){
            period = getString(R.string.period_weekly);
        }else if (detail[2].equals(Period.monthly.toString())){
            period = getString(R.string.period_monthly);
        }
        this.period.setText(period);
        Log.d(TAG, "onCreate: task details set - Title: " + detail[0] + ", Description: " + detail[1] + ", Period: " + detail[2]);


        // chart
        HistroyChartAdapter histroyChartAdapter = new HistroyChartAdapter();
        pieChart = findViewById(R.id.detail_pie_chart);
        pieChart.notifyDataSetChanged();
        pieChart.setData(null);
        pieChart.setData(histroyChartAdapter.pieChartEntry(this, id));
        pieChart.notifyDataSetChanged();


        Description description = new Description();
//        description.setText(getString(R.string.history_chart_description));
        description.setText(" ");
        pieChart.setDescription(description);


        delete.setOnClickListener(v -> {
            Log.d(TAG, "onClick: delete button clicked");

            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.dialog_title_delete_item)
                    .setMessage(R.string.dialog_massage_delete_item)
                    .setPositiveButton("ok", (dialog, which) -> {
                        db.deleteRecord(id);
                        Toast.makeText(this, R.string.delete_task_toast, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: task deleted");

                        Intent intent = new Intent(this, PeriodicTasksActivity.class);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "onClick: navigating to PeriodicTaskActivity");
                    })
                    .setNegativeButton("cancel", null)
                    .create()
                    .show();
        });

        edit.setOnClickListener(v -> {
            Log.d(TAG, "onClick: edit button clicked");

            Bundle bundle = new Bundle();
            bundle.putStringArray("data", detail);
            Fragment fragment = new Fragment();
            fragment.setArguments(bundle);

            PeriodTaskBottomSheet periodTaskBottomSheet = new PeriodTaskBottomSheet();
            periodTaskBottomSheet.setArguments(bundle);
            periodTaskBottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            Log.d(TAG, "onClick: showing PeriodTaskBottomSheet");
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed: back pressed, navigating to PeriodicTaskActivity");
                Intent intent = new Intent(PeriodicTaskDetailActivity.this, PeriodicTasksActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
