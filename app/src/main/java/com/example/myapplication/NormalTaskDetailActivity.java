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
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.bottomsheet.NormalTaskBottomSheet;
import com.example.myapplication.database.TaskDataBase.DeadLinedTaskDB;
import com.example.myapplication.time.ShamsiName;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NormalTaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "NormalTaskDetailActivity";

    private ImageButton edit, delete;
    private TextView title, description, date;
    private LinearLayout layout;
    private DeadLinedTaskDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_normal_task_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initials
        layout = findViewById(R.id.main);
        edit = findViewById(R.id.edit_button_edit_activity);
        delete = findViewById(R.id.delete_button_edit_activity);
        title = findViewById(R.id.task_title_edit_activity);
        description = findViewById(R.id.task_description_edit_activity);
        date = findViewById(R.id.date_text_edit_activity);
        db = new DeadLinedTaskDB(this);
        Log.d(TAG, "onCreate: views initialized");

        int id = getIntent().getIntExtra("task", 0);
        Log.d(TAG, "onCreate: received task ID: " + id);

        Cursor cursor = db.getRecord(id);
        String[] detail = new String[7];
        detail[0] = String.valueOf(id);
        detail[1] = cursor.getString(1);
        detail[2] = cursor.getString(2);
        detail[3] = String.valueOf(cursor.getInt(4));
        detail[4] = String.valueOf(cursor.getInt(5));
        detail[5] = String.valueOf(cursor.getInt(6));

        Log.d(TAG, "onCreate: received data:" +
                " id:" + detail[0] +
                " title:" + detail[1] +
                " description:" + detail[2] +
                " dead day:" + detail[3] +
                " dead month:" + detail[4] +
                " dead year:" + detail[5]);

        title.setText(detail[1]);
        description.setText(detail[2]);
        String s = detail[3] + " " + ShamsiName.getMonthName(Integer.parseInt(detail[4]),this) + " " + detail[5];
        date.setText(s);

        Log.d(TAG, "onCreate: task details set");

        delete.setOnClickListener(v -> {
            Log.d(TAG, "onClick: delete button clicked");

            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.dialog_title_delete_item)
                    .setMessage(R.string.dialog_massage_delete_item)
                    .setPositiveButton("ok", (dialog, which) -> {
                        db.deleteRecord(id);
                        Toast.makeText(this, R.string.delete_task_toast, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: task deleted");

                        Intent intent = new Intent(this, NormalTaskActivity.class);
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

            NormalTaskBottomSheet normalTaskBottomSheet = new NormalTaskBottomSheet();
            normalTaskBottomSheet.setArguments(bundle);
            normalTaskBottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            Log.d(TAG, "onClick: showing PeriodTaskBottomSheet");

        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed: back pressed, navigating to PeriodicTaskActivity");
                Intent intent = new Intent(NormalTaskDetailActivity.this, NormalTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}