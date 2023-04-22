package com.example.vkr1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vkr1.Adapters.DisksAdapter;
import com.example.vkr1.Adapters.GpusAdapter;
import com.example.vkr1.Entity.Computer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    private RecyclerView disksRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DisksAdapter disksAdapter;
    private RecyclerView gpusRecyclerView;
    private GpusAdapter gpusAdapter;


    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView cpu = findViewById(R.id.cpu);
        TextView ram = findViewById(R.id.ram);

        String hardwareId;
        String key;

        //Получение intend
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                hardwareId = null;
                key = null;
                name = null;
            } else {
                hardwareId= extras.getString("Chosen");
                key = extras.getString("Key");
                name = extras.getString("Name");
            }
        } else {
            hardwareId = (String) savedInstanceState.getSerializable("Chosen");
            key = (String) savedInstanceState.getSerializable("Key");
            name = (String) savedInstanceState.getSerializable("Name");
        }

        //Кнопка назад
        ImageButton backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.putExtra("Key", key);
            startActivity(intent);
        });

        TextView textViewName = findViewById(R.id.userName);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://afire.tech:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<Computer> call = jsonPlaceholder.getComputer(hardwareId, key);
        call.enqueue(new Callback<Computer>() {
            @Override
            public void onResponse(Call<Computer> call, Response<Computer> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Home.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Computer computer = response.body();
                Log.e("e", "RESPONSE2  " + computer.getCpu());
                name = computer.getName();
                cpu.setText(computer.getCpu());
                textViewName.setText(name);
                ram.setText(String.valueOf(computer.getRam()));
                initDisksRecyclerView(computer.getDisks());
                initGpusRecyclerView(computer.getGpus());
            }

            @Override
            public void onFailure(Call<Computer> call, Throwable t) {
                Log.e("e", "RESPONSE  " + t.getMessage());
            }
        });



        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId())
                {
                    case R.id.chat:
                        intent = new Intent(Home.this, Chat.class);
                        intent.putExtra("Chosen", hardwareId);
                        intent.putExtra("Key", key);
                        intent.putExtra("Name", name);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.logs:
                        intent = new Intent(Home.this, Logs.class);
                        intent.putExtra("Chosen", hardwareId);
                        intent.putExtra("Key", key);
                        intent.putExtra("Name", name);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }

    private void initDisksRecyclerView(ArrayList<String> diskArrayList){
        disksRecyclerView = findViewById(R.id.disks);
        layoutManager = new GridLayoutManager(this, 2); //2 колонки
        disksRecyclerView.setHasFixedSize(true);
        disksRecyclerView.setLayoutManager(layoutManager);
        disksAdapter = new DisksAdapter(this, diskArrayList);
        disksRecyclerView.setAdapter(disksAdapter);
    }
    private void initGpusRecyclerView(ArrayList<String> gpusArrayList) {
        gpusRecyclerView = findViewById(R.id.gpus);
        layoutManager = new GridLayoutManager(this, 2); //2 колонки
        gpusRecyclerView.setHasFixedSize(true);
        gpusRecyclerView.setLayoutManager(layoutManager);
        gpusAdapter = new GpusAdapter(this, gpusArrayList);
        gpusRecyclerView.setAdapter(gpusAdapter);
    }
}