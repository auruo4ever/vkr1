package com.example.vkr1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vkr1.Entity.Computer;
import com.example.vkr1.Entity.Computers;
import com.example.vkr1.Entity.JSONPlaceholder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView userName = findViewById(R.id.homeText);
        TextView cpu = findViewById(R.id.cpu);
        TextView gpus = findViewById(R.id.gpus);

        //Получение intend
        String hardwareId;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                hardwareId = null;
            } else {
                hardwareId= extras.getString("Chosen");
            }
        } else {
            hardwareId = (String) savedInstanceState.getSerializable("Chosen");
        }


        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://46.151.30.76:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<Computer> call = jsonPlaceholder.getComputer(hardwareId);
        call.enqueue(new Callback<Computer>() {
            @Override
            public void onResponse(Call<Computer> call, Response<Computer> response) {

                Log.e("e", "RESPONSE2  " + response);

                if (!response.isSuccessful()) {
                    Toast.makeText(Home.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Computer computer = response.body();
                Log.e("e", "RESPONSE2  " + computer.getCpu());
                userName.setText(computer.getName());
                cpu.setText("CPU: " + computer.getCpu());
                StringBuilder builder = new StringBuilder();
                for (String disk: computer.getGpus()) {
                    builder.append(disk);
                    builder.append("\n ");
                }
                gpus.setText("GPUS: " + builder.toString());

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
                    case R.id.dashboard:
                        intent = new Intent(Home.this, Dashboard.class);
                        intent.putExtra("Chosen", hardwareId);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.about:
                        intent = new Intent(Home.this, About.class);
                        intent.putExtra("Chosen", hardwareId);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }
}