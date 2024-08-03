package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.database.TaskDB;
import com.example.myapplication.model.TaskModel;
import com.example.myapplication.time.ShamsiMonth;

import java.util.Calendar;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class AddNormalTask extends AppCompatActivity {

    String TAG = "AddNormalTask";

    PersianDatePickerDialog picker;

    private TaskDB db;
    private EditText addTitle, addDescription;
    private TextView addDate;
    private Button addButton, dateButton;

    private int deadDay,deadMonth ,deadYear;
    private boolean dateChoosed = false;

    private TaskModel taskModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_normal_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initials
        addTitle = findViewById(R.id.task_title_edit_text);
        addDescription = findViewById(R.id.task_description_edit_text);
        addDate = findViewById(R.id.task_date_edit_text);
        addButton = findViewById(R.id.add_btn);
        dateButton = findViewById(R.id.choose_date_button);
        Log.d(TAG, "onCreate: views initialized");




        dateButton.setOnClickListener(v -> {
            picker = new PersianDatePickerDialog(AddNormalTask.this)
                    .setPositiveButtonString(getString(R.string.choose_date_ok_text))
                    .setNegativeButton(getString(R.string.choose_date_cancel_text))
                    .setTodayButton(getString(R.string.choose_date_today_text))
                    .setTodayButtonVisible(true)
                    .setInitDate(PersianDatePickerDialog.THIS_YEAR, PersianDatePickerDialog.THIS_MONTH, PersianDatePickerDialog.THIS_DAY)
                    .setActionTextColor(Color.GRAY)
                    .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                    .setShowInBottomSheet(false)
                    .setListener(new PersianPickerListener() {
                        @Override
                        public void onDateSelected( PersianPickerDate persianPickerDate) {
                            deadDay = persianPickerDate.getPersianDay();
                            deadMonth = persianPickerDate.getPersianMonth();
                            deadYear = persianPickerDate.getPersianYear();
                            dateChoosed = true;
                            String s = "  " + deadDay + "  " +
                                    ShamsiMonth.getMonthName(deadMonth, AddNormalTask.this) + "  " +
                                    deadYear + "  ";
                            addDate.setText(s);
                            Log.d(TAG, "date = " + persianPickerDate.getPersianDay() + "/" +
                                persianPickerDate.getPersianMonth() + "/" +
                                    persianPickerDate.getPersianYear());
                        }

                        @Override
                        public void onDismissed() {

                        }
                    });
            picker.show();
        });




// database
        db = new TaskDB(this);
        Log.d(TAG, "onCreate: database initialized");

        addButton.setOnClickListener(v -> {
            Log.d(TAG, "onClick: add button clicked");

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(addTitle.getText());
            checkBox.setChecked(false);

            taskModel = new TaskModel(
                    checkBox, addDescription.getText().toString(), 0 , deadDay, deadMonth, deadYear);


            if(dateChoosed){
            Log.d(TAG, "onClick: title: " + taskModel.getCheckBox().getText() + ", description: " + taskModel.getDescription());


                db.insertRecord(taskModel.getCheckBox().getText().toString() , taskModel.getDescription(), 0,
                        taskModel.getDeadDay(), taskModel.getDeadMonth(), taskModel.getDeadYear());

                Log.d(TAG, "onClick: record inserted into database");

                Toast.makeText(AddNormalTask.this, getString(R.string.add_successfully), Toast.LENGTH_LONG).show();
                Log.i(TAG, "onClick: task added successfully");

                Intent intent = new Intent(this, NormalTaskActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "onClick: navigating to PeriodicTaskActivity");

            } else {
                Toast.makeText(AddNormalTask.this, getString(R.string.choose_date_error), Toast.LENGTH_LONG).show();
                Log.w(TAG, "onClick: period not chosen, showing error toast");
            }
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed: back pressed, navigating to PeriodicTaskActivity");
                Intent intent = new Intent(AddNormalTask.this, NormalTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}