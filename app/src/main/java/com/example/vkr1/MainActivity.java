package com.example.vkr1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private int[] images = {R.drawable.computer, R.drawable.computer, R.drawable.computer, R.drawable.computer};
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MainRecyclerAdapter adapter;
    private Integer current = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.e("e", "ABOBA  " + token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initRecyclerView(){
        MainRecyclerAdapter.OnComputerClickListener onTourClickListener = new MainRecyclerAdapter.OnComputerClickListener() {
            @Override
            public void OnTaskClick(int computer) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                //Intent intent2 = new Intent("Chosen Tour");
                //current = computer;
                //intent2.putExtra("Chosen", computer);
                intent.putExtra("Chosen", computer);
               // LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent2);
                startActivity(intent);
            }
        };
        recyclerView = findViewById(R.id.recyclerViewMain);
        layoutManager = new GridLayoutManager(this, 2); //2 колонки
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainRecyclerAdapter(this, images, onTourClickListener);
        recyclerView.setAdapter(adapter);

    }
}