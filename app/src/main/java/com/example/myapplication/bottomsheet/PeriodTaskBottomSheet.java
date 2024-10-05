package com.example.myapplication.bottomsheet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.model.Period;
import com.example.myapplication.PeriodicTasksActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.TaskDataBase.HabitDB;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class PeriodTaskBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "PeriodTaskBottomSheet";


    private HabitDB db;
    private EditText editTitle, editDescription;
    AutoCompleteTextView editPeriod;
    private Button editButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");

        View v = inflater.inflate(R.layout.period_task_bottom_sheet, container, false);

        editTitle = v.findViewById(R.id.edit_task_title_edit_text);
        editDescription = v.findViewById(R.id.edit_task_description_edit_text);
        editButton = v.findViewById(R.id.edit_btn);
        editPeriod = v.findViewById(R.id.edit_autoCompleteTextView);


        // set default value
        assert this.getArguments() != null;
        String[] data = this.getArguments().getStringArray("data");
        if (data != null) {
            Log.d(TAG, "onCreateView: received data - " + java.util.Arrays.toString(data));

            editTitle.setText(data[0]);
            editDescription.setText(data[1]);
            String period = "";
            if (data[2].equals(Period.daily.toString())) {
                period = getString(R.string.period_daily);
            } else if (data[2].equals(Period.weekly.toString())) {
                period = getString(R.string.period_weekly);
            } else if (data[2].equals(Period.monthly.toString())) {
                period = getString(R.string.period_monthly);
            }
            editPeriod.setText(period);
        } else {
            Log.w(TAG, "onCreateView: no data received");
        }

        // create dropdown menu
        String[] languages = getResources().getStringArray(R.array.period);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(v.getContext(), R.layout.drop_down_item, languages);
        editPeriod.setAdapter(arrayAdapter);
        Log.d(TAG, "onCreateView: dropdown menu created");


        db = new HabitDB(v.getContext());
        Log.d(TAG, "onCreateView: database initialized");


        // update values
        editButton.setOnClickListener(l -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            String period = editPeriod.getText().toString();

            if (period.equals(getString(R.string.period_daily))) {
                period = Period.daily.toString();
            } else if (period.equals(getString(R.string.period_weekly))) {
                period = Period.weekly.toString();
            } else if (period.equals(getString(R.string.period_monthly))) {
                period = Period.monthly.toString();
            }

            assert data != null;
            int id = Integer.parseInt(data[3]);

            Log.d(TAG, "onClick: updating record with ID " + id);


            Cursor cursor = db.getRecord(id);
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            int week = cursor.getInt(cursor.getColumnIndexOrThrow("week"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));

            Log.d(TAG, "onClick: current values - year = " + year);


            db.updateRecord(id, title, description, period, day, week, month, year);
            Log.d(TAG, "onClick: record updated");

            Toast.makeText(v.getContext(), getString(R.string.edit_successfully),
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onClick: edit successful");


            Intent intent = new Intent(v.getContext(), PeriodicTasksActivity.class);
            startActivity(intent);
            Log.d(TAG, "onClick: navigating to PeriodicTaskActivity");

        });

        return v;
    }
}
