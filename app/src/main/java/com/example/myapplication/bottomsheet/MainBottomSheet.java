package com.example.myapplication.bottomsheet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myapplication.AddNormalTask;
import com.example.myapplication.AddPeriodTask;
import com.example.myapplication.AddSimpleTask;
import com.example.myapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "MainBottomSheet";

    Button timedButton, simpleButton, periodicButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");

        View v = inflater.inflate(R.layout.main_bottom_sheet, container, false);

        timedButton = v.findViewById(R.id.timed_task_btn);
        simpleButton = v.findViewById(R.id.simple_task_btn);
        periodicButton = v.findViewById(R.id.periodic_task_btn);

        timedButton.setOnClickListener(v1 -> {
            Intent i = new Intent(getContext(), AddNormalTask.class);
            startActivity(i);
        });
        simpleButton.setOnClickListener(v1 -> {
            Intent i = new Intent(getContext(), AddSimpleTask.class);
            startActivity(i);
        });
        periodicButton.setOnClickListener(v1 -> {
            Intent i = new Intent(getContext(), AddPeriodTask.class);
            startActivity(i);
        });

        return v;
    }
}
