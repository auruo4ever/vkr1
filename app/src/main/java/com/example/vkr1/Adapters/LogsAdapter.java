package com.example.vkr1.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vkr1.Entity.Log;
import com.example.vkr1.R;

import java.util.ArrayList;

public class LogsAdapter extends RecyclerView.Adapter {

    private ArrayList<Log> logsArrayList;
    private Context context;


    public ArrayList<Log> getLogsArrayList() {
        return logsArrayList;
    }

    public LogsAdapter(ArrayList<Log> logsArrayList, Context context) {
        this.logsArrayList = logsArrayList;
        this.context = context;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item_layout, parent, false);
        return new LogsAdapter.LogsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log log = logsArrayList.get(position);
        LogsAdapter.LogsViewHolder viewHolder = (LogsAdapter.LogsViewHolder)holder;
        viewHolder.logName.setText(log.getLogTypeName());
        viewHolder.time.setText(log.getNormalDate());
        viewHolder.image.setText(log.getLogTypeFirstLetter());
        viewHolder.text.setText(log.getLogText());
        GradientDrawable backgroundGradient = (GradientDrawable)viewHolder.image.getBackground();
        if (log.getLogType() == 0) {
            backgroundGradient.setColor(context.getResources().getColor(R.color.green));
        }
        if (log.getLogType() == 1) {
            backgroundGradient.setColor(context.getResources().getColor(R.color.red));
        }
        if (log.getLogType() == 2) {
            backgroundGradient.setColor(context.getResources().getColor(R.color.teal_200));
        }
        if (log.getLogType() == 3) {
            backgroundGradient.setColor(context.getResources().getColor(R.color.blue));
        }
        if (log.getLogType() == 4) {
            backgroundGradient.setColor(context.getResources().getColor(R.color.yellow));
        }
        if (log.getLogType() == 5) {
            backgroundGradient.setColor(context.getResources().getColor(R.color.purple_500));
        }

    }



    @Override
    public int getItemCount() {
        return logsArrayList.size();
    }



    class LogsViewHolder extends RecyclerView.ViewHolder
    {

        TextView logName;
        TextView time;
        TextView image;
        TextView text;


        public LogsViewHolder(@NonNull View itemView) {
            super(itemView);
            logName = itemView.findViewById(R.id.logName);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
        }
    }

    public void setColor() {

    }


}
