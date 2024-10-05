package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.GetAllTask;
import com.example.myapplication.database.TaskDataBase.DeadLinedTaskDB;
import com.example.myapplication.recadapter.TaskAdapter;


public class NormalTaskPastFragment extends Fragment {
    private static final String TAG = "NormalTaskActivity:PastFragment";

    RecyclerView pastRecyclerView;
    DeadLinedTaskDB db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal_task_past, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DeadLinedTaskDB(getActivity());
        pastRecyclerView = view.findViewById(R.id.past_recycler_view);
        TaskAdapter taskAdapter = new TaskAdapter(GetAllTask.pastTasks(getActivity()), db);
        pastRecyclerView.setHasFixedSize(true);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pastRecyclerView.setAdapter(taskAdapter);
        Log.d(TAG, "onViewCreated: daily tasks recycler view set up");

    }
}