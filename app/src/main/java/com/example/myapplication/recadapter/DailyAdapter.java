package com.example.myapplication.recadapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.EditDelete;
import com.example.myapplication.R;
import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.recyclerview.PeriodicModel;

import java.util.Calendar;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {
    private static final String TAG = "DailyAdapter";

    private PeriodicModel[] listdata;
    private RoutineDB db;

    public DailyAdapter(PeriodicModel[] listdata, RoutineDB db) {
        this.listdata = listdata;
        this.db = db;
        Log.d(TAG, "DailyAdapter: Adapter created with " + listdata.length + " items");
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

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        JalaliDateTime jalaliDateTime = JalaliDateTime.Now();

        int day = jalaliDateTime.getDay();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int month = jalaliDateTime.getMonth();

        PeriodicModel model = listdata[position];
        Log.d(TAG, "onBindViewHolder: model ID " + model.getId() + ", description: " + model.getDescription());

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setText(model.getCheckBox().getText());
        holder.checkBox.setChecked(model.getCheckBox().isChecked());
        Log.d(TAG, "onBindViewHolder: set checkbox text to " + model.getCheckBox().getText() + " and checked state to " + model.getCheckBox().isChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "onCheckedChanged: checkbox with text " + model.getCheckBox().getText() + " changed to " + isChecked);
            model.getCheckBox().setChecked(isChecked);
            db.updateRecord(
                    model.getId(),
                    model.getCheckBox().getText().toString(),
                    model.getDescription(),
                    model.getPeriod().toString(),
                    model.getCheckBox().isChecked() ? 1 : 0,
                    day,
                    week,
                    month);
            Log.d(TAG, "onCheckedChanged: updated record in database for ID " + model.getId());
        });

        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: item clicked with ID " + model.getId());
            Intent intent = new Intent(v.getContext(), EditDelete.class);
            intent.putExtra("task", model.getId());
            v.getContext().startActivity(intent);
            Log.d(TAG, "onClick: started EditDelete activity for ID " + model.getId());
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
