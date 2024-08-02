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
import com.example.myapplication.database.TaskDB;
import com.example.myapplication.recadapter.TaskAdapter;


public class NormalTaskFutureFragment extends Fragment {
    private static final String TAG = "NormalTaskActivity:FutureFragment";

    RecyclerView futureRecyclerView;
    TaskDB db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal_task_future, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new TaskDB(getActivity());
        futureRecyclerView = view.findViewById(R.id.future_recycler_view);
        TaskAdapter taskAdapter = new TaskAdapter(GetAllTask.futureTasks(getActivity()), db);
        futureRecyclerView.setHasFixedSize(true);
        futureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        futureRecyclerView.setAdapter(taskAdapter);
        Log.d(TAG, "onViewCreated: daily tasks recycler view set up");


    }
}