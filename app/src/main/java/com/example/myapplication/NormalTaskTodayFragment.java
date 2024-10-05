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


public class NormalTaskTodayFragment extends Fragment {
    private static final String TAG = "NormalTaskActivity:TodayFragment";

    RecyclerView todayRecyclerView;
    DeadLinedTaskDB db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal_task_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DeadLinedTaskDB(getActivity());
        todayRecyclerView = view.findViewById(R.id.today_recycler_view);
        TaskAdapter taskAdapter = new TaskAdapter(GetAllTask.todayTasks(getActivity()), db);
        todayRecyclerView.setHasFixedSize(true);
        todayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        todayRecyclerView.setAdapter(taskAdapter);
        Log.d(TAG, "onViewCreated: daily tasks recycler view set up");



    }

}