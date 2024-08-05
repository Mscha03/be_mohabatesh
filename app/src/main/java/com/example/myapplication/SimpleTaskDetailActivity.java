package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.myapplication.bottomsheet.SimpleTaskBottomSheet;
import com.example.myapplication.database.SimpleDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SimpleTaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "SimpleTaskDetailActivity";

    private ImageButton edit, delete;
    private TextView title, description;
    private SimpleDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_simple_task_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initials
        edit = findViewById(R.id.edit_button_edit_activity);
        delete = findViewById(R.id.delete_button_edit_activity);
        title = findViewById(R.id.task_title_edit_activity);
        description = findViewById(R.id.task_description_edit_activity);
        db = new SimpleDB(this);
        Log.d(TAG, "onCreate: views initialized");

        int id = getIntent().getIntExtra("task", 0);
        Log.d(TAG, "onCreate: received task ID: " + id);

        Cursor cursor = db.getRecord(id);
        String[] detail = new String[3];
        detail[0] = String.valueOf(id);
        detail[1] = cursor.getString(1);
        detail[2] = cursor.getString(2);

        Log.d(TAG, "onCreate: received data:" +
                " id:" + detail[0] +
                " title:" + detail[1] +
                " description:" + detail[2]);

        title.setText(detail[1]);
        description.setText(detail[2]);


        delete.setOnClickListener(v -> {
            Log.d(TAG, "onClick: delete button clicked");

            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.dialog_title_delete_item)
                    .setMessage(R.string.dialog_massage_delete_item)
                    .setPositiveButton("ok", (dialog, which) -> {
                        db.deleteRecord(id);
                        Toast.makeText(this, R.string.delete_task_toast, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: task deleted");

                        Intent intent = new Intent(this, SimpleTaskActivity.class);
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

            SimpleTaskBottomSheet simpleTaskBottomSheet = new SimpleTaskBottomSheet();
            simpleTaskBottomSheet.setArguments(bundle);
            simpleTaskBottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            Log.d(TAG, "onClick: showing SimpleTaskBottomSheet");

        });
    }
}