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

import com.example.vkr1.Entity.Computer;
import com.example.vkr1.Entity.Computers;
import com.example.vkr1.Entity.JSONPlaceholder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MainRecyclerAdapter adapter;
    private Integer current = 0;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Получение intend

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                key = null;
            } else {
                key= extras.getString("Key");
            }
        } else {
            key = (String) savedInstanceState.getSerializable("Chosen");
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://46.151.30.76:5000/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                                .build();
        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<Computers> call = jsonPlaceholder.getComputers(key);
        call.enqueue(new Callback<Computers>() {
            @Override
            public void onResponse(Call<Computers> call, Response<Computers> response) {

                //Log.e("e", "RESPONSE  " + response);

                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Computers computers = response.body();
                initRecyclerView(computers);
            }

            @Override
            public void onFailure(Call<Computers> call, Throwable t) {
                Log.e("e", "RESPONSE  " + t.getMessage());
            }
        });


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
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initRecyclerView(Computers computers){
        MainRecyclerAdapter.OnComputerClickListener onTourClickListener = new MainRecyclerAdapter.OnComputerClickListener() {
            @Override
            public void OnTaskClick(int computernum) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                String hardwareId = computers.getComputers().get(computernum).getHardware_id();
                intent.putExtra("Chosen", hardwareId);
                intent.putExtra("Key", key);
               // LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent2);
                startActivity(intent);
            }
        };
        recyclerView = findViewById(R.id.recyclerViewMain);
        layoutManager = new GridLayoutManager(this, 2); //2 колонки
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainRecyclerAdapter(this, computers, onTourClickListener);
        recyclerView.setAdapter(adapter);

    }
} 