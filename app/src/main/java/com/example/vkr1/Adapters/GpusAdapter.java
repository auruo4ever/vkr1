package com.example.vkr1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vkr1.R;

import java.util.ArrayList;

public class GpusAdapter extends RecyclerView.Adapter<GpusAdapter.RecyclerViewHolder> {
    private ArrayList<String> gpusArrayList;
    private Context mcontext;

    public GpusAdapter(Context mcontext, ArrayList<String> gpusArrayList) {
        this.gpusArrayList = gpusArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gpus_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        String gpus = gpusArrayList.get(position);
        holder.gpusName.setText(gpus);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return gpusArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView gpusName;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            gpusName = itemView.findViewById(R.id.gpusName);
        }
    }
}
