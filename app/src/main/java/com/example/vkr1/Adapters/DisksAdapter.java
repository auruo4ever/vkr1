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

public class DisksAdapter extends RecyclerView.Adapter<DisksAdapter.RecyclerViewHolder> {
    private ArrayList<String> diskArrayList;
    private Context mcontext;

    public DisksAdapter(Context mcontext, ArrayList<String> diskArrayList) {
        this.diskArrayList = diskArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disks_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        String disk = diskArrayList.get(position);
        holder.diskName.setText(disk.substring(0, 2));
        holder.diskSize.setText(disk.substring(2) + " ГБ");
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return diskArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView diskName;
        private TextView diskSize;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            diskName = itemView.findViewById(R.id.diskName);
            diskSize = itemView.findViewById(R.id.diskSize);
        }
    }
}
