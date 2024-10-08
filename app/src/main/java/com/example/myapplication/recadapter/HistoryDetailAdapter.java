package com.example.myapplication.recadapter;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.HabitHistoryItemModel;

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder>{

    private static final String TAG = "PeriodAdapter";

    private final HabitHistoryItemModel[] listdata;

    public HistoryDetailAdapter(HabitHistoryItemModel[] listdata) {
        this.listdata = listdata;
        Log.d(TAG, "HistoryDetailAdapter: Adapter created with " + listdata.length + " items");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: creating view holder");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_history_detail, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: binding view holder at position " + position);

        HabitHistoryItemModel model = listdata[position];
        Log.d(TAG, "onBindViewHolder: Day = " + model.getChangeDay() +
                                    " Week = " + model.getChangeWeek() +
                                    " Month = " + model.getChangeMonth() +
                                    " Year = " + model.getChangeYear() +
                                    " Done = " + model.getIsDone());

        holder.dateText.setText(titleCreator(model));
        Log.d(TAG, "onBindViewHolder: set checkbox text to " + titleCreator(model));

        Drawable background = holder.layout.getBackground();
        GradientDrawable gradientDrawable = (GradientDrawable) background;
        gradientDrawable.setColor(ContextCompat.getColor(holder.layout.getContext(), setBackgroundColor(model)));
        Log.d(TAG, "onBindViewHolder: set background color to " + setBackgroundColor(model));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: returning item count " + listdata.length);
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView dateText;
        public LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateText = itemView.findViewById(R.id.item_checkbox);
            this.layout = itemView.findViewById(R.id.history_item_detail_layout);
            Log.d(TAG, "ViewHolder: created view holder");
        }
    }

    private String titleCreator(HabitHistoryItemModel model) {
        String s = "";

        switch (model.getPeriod()){
            case daily:
                s = model.getChangeYear() + " / " + model.getChangeMonth() + " / " + model.getChangeDay();
                break;

            case weekly:
                s = model.getChangeYear() + " - " + model.getChangeWeek();
                break;

            case monthly:
                s = model.getChangeYear() + " / " + model.getChangeMonth();
                break;
            default:
                break;
        }
        return s;
    }

    private int setBackgroundColor(HabitHistoryItemModel model){
        int i;

        switch (model.getIsDone()){
            case 0:
                i = R.color.gray;
                break;

            case 1:
                i = R.color.green;
                break;

            case 2:
                i = R.color.yellow;
                break;

            case 3:
                i = R.color.red;
                break;

            default:
                i = R.color.red;

        }

        return i;
    }
}
