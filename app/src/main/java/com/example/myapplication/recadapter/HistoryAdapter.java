package com.example.myapplication.recadapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.HistoryDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.tasks.habits.Habit;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private static final String TAG = "PeriodAdapter";

    private final Habit[] listdata;

    public HistoryAdapter(Habit[] listdata) {
        this.listdata = listdata;
        Log.d(TAG, "PeriodAdapter: Adapter created with " + listdata.length + " items");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: creating view holder");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_history, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: binding view holder at position " + position);

        Habit model = listdata[position];
        Log.d(TAG, "onBindViewHolder: model ID " + model.getId() + ", description: " + model.getDescription());

        holder.checkBox.setText(model.getTitle());
        Log.d(TAG, "onBindViewHolder: set checkbox text to " + model.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: item clicked with ID " + model.getId());
            Intent intent = new Intent(v.getContext(), HistoryDetailActivity.class);
            intent.putExtra("taskId", model.getId());
            intent.putExtra("taskName", model.getTitle());
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

        public TextView checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.item_checkbox);
            Log.d(TAG, "ViewHolder: created view holder");
        }
    }


}
