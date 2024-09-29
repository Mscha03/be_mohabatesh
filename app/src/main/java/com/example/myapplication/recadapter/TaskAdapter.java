package com.example.myapplication.recadapter;

import static com.example.myapplication.changer.BoolInt.boolToInt;
import static com.example.myapplication.changer.BoolInt.intToBool;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NormalTaskDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.TaskDB;
import com.example.myapplication.model.tasks.DeadLinedTask;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private static final String TAG = "TaskAdapter";

    private final DeadLinedTask[] listdata;
    private final TaskDB db;

    public TaskAdapter(DeadLinedTask[] listdata, TaskDB db) {
        this.listdata = listdata;
        this.db = db;
        Log.d(TAG, "PeriodAdapter: Adapter created with " + listdata.length + " items");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: creating view holder");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: binding view holder at position " + position);

        DeadLinedTask model = listdata[position];
        Log.d(TAG, "onBindViewHolder: model ID " + model.getId() + ", description: " + model.getDescription());

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setText(model.getTitle());
        holder.checkBox.setChecked(intToBool(model.getIsDone()));
        Log.d(TAG, "onBindViewHolder: set checkbox text to " + model.getTitle() + " and checked state to " + model.getIsDone());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "onCheckedChanged: checkbox with text " + model.getTitle() + " changed to " + isChecked);
            model.setIsDone(boolToInt(isChecked));
            db.updateRecord(
                    model.getId(),
                    model.getTitle(),
                    model.getDescription(),
                    model.getIsDone(),
                    model.getDeadDate().getDay(),
                    model.getDeadDate().getMonth(),
                    model.getDeadDate().getYear());
            Log.d(TAG, "onCheckedChanged: updated record in database for ID " + model.getId());
        });

        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: item clicked with ID " + model.getId());
            Intent intent = new Intent(v.getContext(), NormalTaskDetailActivity.class);
            intent.putExtra("task", model.getId());
            v.getContext().startActivity(intent);
            Log.d(TAG, "onClick: started PeriodicTaskDetailActivity activity for ID " + model.getId());
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: returning item count " + listdata.length);
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.item_checkbox);
            Log.d(TAG, "ViewHolder: created view holder");
        }
    }
}
