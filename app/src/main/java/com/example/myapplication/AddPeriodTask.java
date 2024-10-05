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
import com.example.myapplication.database.AddInformationForHistory;
import com.example.myapplication.database.TaskDataBase.HabitDB;
import com.example.myapplication.model.Period;

import java.util.Calendar;

public class AddPeriodTask extends AppCompatActivity {

    private static final String TAG = "AddPeriodTask";

    private HabitDB db;
    private EditText addTitle, addDescription;
    private AutoCompleteTextView addPeriod;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: started");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_period_task);
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


        addButton.setOnClickListener(v -> {
            Log.d(TAG, "onClick: add button clicked");

            String title = addTitle.getText().toString();
            String description = addDescription.getText().toString();
            String period = addPeriod.getText().toString();

            Log.d(TAG, "onClick: title: " + title + ", description: " + description + ", period: " + period);

            if (!getString(R.string.choose_period_drop_down_title).equals(period)) {

                addHabit(title, description, period);

                Toast.makeText(AddPeriodTask.this, getString(R.string.add_successfully), Toast.LENGTH_LONG).show();
                Log.i(TAG, "onClick: task added successfully");

                Intent intent = new Intent(this, PeriodicTasksActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "onClick: navigating to PeriodicTaskActivity");

            } else {
                Toast.makeText(AddPeriodTask.this, getString(R.string.choose_period_error), Toast.LENGTH_LONG).show();
                Log.w(TAG, "onClick: period not chosen, showing error toast");
            }
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed: back pressed, navigating to PeriodicTaskActivity");
                Intent intent = new Intent(AddPeriodTask.this, PeriodicTasksActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }




    private void addHabit(
            String name, String description, String period
    ){
        // database
        db = new HabitDB(this);
        Log.d(TAG, "addHabit: database initialized");

        if (period.equals(getString(R.string.period_daily))){
            period = Period.daily.toString();
        }else if (period.equals(getString(R.string.period_weekly))){
            period = Period.weekly.toString();
        }else if (period.equals(getString(R.string.period_monthly))){
            period = Period.monthly.toString();
        }

        JalaliDateTime jalaliDateTime = JalaliDateTime.Now();
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);

        long id = db.insertRecord(name, description, period,
                jalaliDateTime.getDay(),
                calendar.get(Calendar.WEEK_OF_YEAR),
                jalaliDateTime.getMonth(),
                jalaliDateTime.getYear()
                );

        AddInformationForHistory.addDays(period, id, db);
        Log.d(TAG, "onClick: record inserted into database");
    }




}
