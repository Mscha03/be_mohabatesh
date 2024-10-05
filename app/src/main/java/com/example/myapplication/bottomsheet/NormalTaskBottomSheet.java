package com.example.myapplication.bottomsheet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.NormalTaskActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.TaskDataBase.DeadLinedTaskDB;
import com.example.myapplication.time.ShamsiName;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class NormalTaskBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "NormalTaskBottomSheet";

    private DeadLinedTaskDB db;
    private EditText editTitle, editDescription;
    private TextView dateText;
    private Button edit, dateButton;

    PersianDatePickerDialog picker;

    int deadDay, deadMonth, deadYear;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");

        View v = inflater.inflate(R.layout.normal_task_bottom_sheet, container, false);

        editTitle = v.findViewById(R.id.edit_task_title_edit_text);
        editDescription = v.findViewById(R.id.edit_task_description_edit_text);
        dateText = v.findViewById(R.id.edit_task_date_textview);
        edit = v.findViewById(R.id.edit_btn);
        dateButton = v.findViewById(R.id.choose_date_button);

        // set default value
        assert this.getArguments() != null;
        String[] data = this.getArguments().getStringArray("data");
        if (data != null) {
            Log.d(TAG, "onCreateView: received data - " + java.util.Arrays.toString(data));

            editTitle.setText(data[1]);
            editDescription.setText(data[2]);
            deadDay = Integer.parseInt(data[3]);
            deadMonth = Integer.parseInt(data[4]);
            deadYear = Integer.parseInt(data[5]);
            String s = deadDay + " " + ShamsiName.getMonthName(deadMonth, v.getContext()) + " " + deadYear;
            dateText.setText(s);

        } else {
            Log.w(TAG, "onCreateView: no data received");
        }


        dateButton.setOnClickListener(m -> {
            picker = new PersianDatePickerDialog(v.getContext())
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
                        public void onDateSelected(PersianPickerDate persianPickerDate) {
                            deadDay = persianPickerDate.getPersianDay();
                            deadMonth = persianPickerDate.getPersianMonth();
                            deadYear = persianPickerDate.getPersianYear();
                            String s = "  " + persianPickerDate.getPersianDay() + "  " +
                                    persianPickerDate.getPersianMonthName() + "  " +
                                    persianPickerDate.getPersianYear() + "  ";
                            dateText.setText(s);
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


        db = new DeadLinedTaskDB(v.getContext());
        Log.d(TAG, "onCreateView: database initialized");

        // update values
        edit.setOnClickListener(l -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();


            assert data != null;
            int id = Integer.parseInt(data[0]);

            Log.d(TAG, "onClick: updating record with ID " + id);

            Cursor cursor = db.getRecord(id);
            int isDone = cursor.getInt(cursor.getColumnIndexOrThrow("isdone"));


            db.updateRecord(id, title, description, isDone, deadDay, deadMonth, deadYear);
            Log.d(TAG, "onClick: record updated");

            Toast.makeText(v.getContext(), getString(R.string.edit_successfully),
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onClick: edit successful");


            Intent intent = new Intent(v.getContext(), NormalTaskActivity.class);
            startActivity(intent);
            Log.d(TAG, "onClick: navigating to PeriodicTaskActivity");

        });

        return v;

    }
}
