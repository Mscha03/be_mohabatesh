package com.example.myapplication.bottomsheet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.SimpleTaskActivity;
import com.example.myapplication.database.TaskDataBase.SimpleTaskDB;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SimpleTaskBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "NormalTaskBottomSheet";

    private SimpleTaskDB db;
    private EditText editTitle, editDescription;
    private Button edit;
    //        FloatingActionButton fab = findViewById(R.id.fab);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");

        View v = inflater.inflate(R.layout.simple_task_bottom_sheet, container, false);

        editTitle = v.findViewById(R.id.edit_task_title_edit_text);
        editDescription = v.findViewById(R.id.edit_task_description_edit_text);
        edit = v.findViewById(R.id.edit_btn);

        // set default value
        assert this.getArguments() != null;
        String[] data = this.getArguments().getStringArray("data");

        if (data != null) {
            Log.d(TAG, "onCreateView: received data - " + java.util.Arrays.toString(data));

            editTitle.setText(data[1]);
            editDescription.setText(data[2]);


        } else {
            Log.w(TAG, "onCreateView: no data received");
        }


        db = new SimpleTaskDB(v.getContext());
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

            db.updateRecord(id, title, description, isDone);
            Log.d(TAG, "onClick: record updated");

            Toast.makeText(v.getContext(), getString(R.string.edit_successfully),
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onClick: edit successful");


            Intent intent = new Intent(v.getContext(), SimpleTaskActivity.class);
            startActivity(intent);
            Log.d(TAG, "onClick: navigating to SimpleTaskActivity");

        });

        return v;

    }

}


