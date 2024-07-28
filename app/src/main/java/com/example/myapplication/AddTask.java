package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.database.RoutineDB;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {

    private static final String TAG = "AddTask";

    private RoutineDB db;
    private EditText addTitle, addDescription;
    private AutoCompleteTextView addPeriod;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initials
        addTitle = findViewById(R.id.task_title_edit_text);
        addDescription = findViewById(R.id.task_description_edit_text);
        addButton = findViewById(R.id.add_btn);
        addPeriod = findViewById(R.id.autoCompleteTextView);
        Log.d(TAG, "onCreate: views initialized");

        // show drop_down_list
        String[] periodList = getResources().getStringArray(R.array.period);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.drop_down_item, periodList);
        addPeriod.setAdapter(arrayAdapter);
        Log.d(TAG, "onCreate: dropdown list set");

        // database
        db = new RoutineDB(this);
        Log.d(TAG, "onCreate: database initialized");

        addButton.setOnClickListener(v -> {
            Log.d(TAG, "onClick: add button clicked");

            String title = addTitle.getText().toString();
            String description = addDescription.getText().toString();
            String period = addPeriod.getText().toString();

            Log.d(TAG, "onClick: title: " + title + ", description: " + description + ", period: " + period);

            if (!getString(R.string.choose_period_drop_down_title).equals(period)) {
                int isDone = 0;

                if (period.equals(getString(R.string.period_daily))){
                    period = Period.daily.toString();
                }else if (period.equals(getString(R.string.period_weekly))){
                    period = Period.weekly.toString();
                }else if (period.equals(getString(R.string.period_monthly))){
                    period = Period.monthly.toString();
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.SATURDAY);
                JalaliDateTime jalaliDateTime = JalaliDateTime.Now();

                int day = jalaliDateTime.getDay();
                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                int month = jalaliDateTime.getMonth();

                Log.d(TAG, "onClick: current date - day: " + day + ", week: " + week + ", month: " + month);

                db.insertRecord(title, description, period, isDone, day, week, month);
                Log.d(TAG, "onClick: record inserted into database");

                Toast.makeText(AddTask.this, getString(R.string.add_successfully), Toast.LENGTH_LONG).show();
                Log.i(TAG, "onClick: task added successfully");

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "onClick: navigating to MainActivity");

            } else {
                Toast.makeText(AddTask.this, getString(R.string.choose_period_error), Toast.LENGTH_LONG).show();
                Log.w(TAG, "onClick: period not chosen, showing error toast");
            }
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed: back pressed, navigating to MainActivity");
                Intent intent = new Intent(AddTask.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
