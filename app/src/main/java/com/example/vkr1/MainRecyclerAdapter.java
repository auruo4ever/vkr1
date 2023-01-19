package com.example.vkr1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ImageViewHolder> {
    private MainRecyclerAdapter.OnComputerClickListener onComputerClickListener;
    private int[] images;
    Context context;

    public MainRecyclerAdapter(Context context, int[] images, MainRecyclerAdapter.OnComputerClickListener onComputerClickListener) {
        this.images = images;
        this.context = context;
        this.onComputerClickListener = onComputerClickListener;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.computers_layout, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        int imageId = images[position];
        holder.imageView.setImageResource(imageId);
        holder.textView.setText("Computer " + position);

        int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int chosen = images[pos];
                //Log.i("TAG1","I will send task number "+ Integer.toString(chosen));
                onComputerClickListener.OnTaskClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.computer);
            textView = itemView.findViewById(R.id.computer_title);
        }
        @Override
        public void onClick(View view) {

        }
    }
    public interface OnComputerClickListener {
        void OnTaskClick(int computer);
    }
}
