package com.example.myapplication.recadapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.Period;
import com.example.myapplication.PeriodicTaskDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.customwidget.MultiStateCheckBox;
import com.example.myapplication.database.RoutineDB;
import com.example.myapplication.model.PeriodicModel;
import java.util.Calendar;

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.ViewHolder> {
    private static final String TAG = "PeriodAdapter";

    private final PeriodicModel[] listdata;
    private final RoutineDB db;

    static Calendar calendar = Calendar.getInstance();
    static JalaliDateTime jalaliDateTime = JalaliDateTime.Now();

    private static int day;
    private static int week;
    private static int month;
    private static int year;

    public PeriodAdapter(PeriodicModel[] listdata, RoutineDB db) {
        this.listdata = listdata;
        this.db = db;
        Log.d(TAG, "PeriodAdapter: Adapter created with " + listdata.length + " items");

        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        resetTime();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: creating view holder");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_periodic, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: binding view holder at position " + position);


        PeriodicModel model = listdata[position];
        Log.d(TAG, "onBindViewHolder: model ID " + model.getId() + ", description: " + model.getDescription());

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setText(model.getCheckBox().getText());
        holder.checkBox.setState(model.getCheckBox().getState());
        Log.d(TAG, "onBindViewHolder: set checkbox text to " + model.getCheckBox().getText() + " and checked state to " + model.getCheckBox().isChecked());


        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            int state = (holder.checkBox.getState() + 1) % 4;
            model.getCheckBox().setState(state);

            zeroExtraNumbers(model.getPeriod());

            db.updateDays(model.getId(),
                    model.getCheckBox().getState(),
                    day,
                    week,
                    month,
                    year);

            Log.d(TAG, "setOnCheckedChangeListener: data base updated: " + model.getCheckBox().getText() + " and checked state to " + model.getCheckBox().getState() +
                    " in day: " + day + " - week: " + week + " - month: " + month + " - year: " + year + " - id: " + model.getId());

            resetTime();
        });


        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: item clicked with ID " + model.getId());
            Intent intent = new Intent(v.getContext(), PeriodicTaskDetailActivity.class);
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

        public MultiStateCheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.item_checkbox);
            Log.d(TAG, "ViewHolder: created view holder");
        }
    }

    private static void zeroExtraNumbers(Period period) {
        switch (period.toString()) {
            case "daily":
                week = 0;
                break;

            case "weekly":
                day = 0;
                month = 0;
                break;

            case "monthly":
                day = 0;
                week = 0;
                break;
            default:
                break;
        }
    }

    private static void resetTime(){
        day = jalaliDateTime.getDay();
        week = calendar.get(Calendar.WEEK_OF_YEAR);
        month = jalaliDateTime.getMonth();
        year = jalaliDateTime.getYear();
    }
}
