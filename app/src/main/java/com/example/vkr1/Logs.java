package com.example.vkr1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Logs extends AppCompatActivity {
    private String key;
    private String hardwareId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

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
            Intent intent = new Intent(Logs.this, MainActivity.class);
            intent.putExtra("Key", key);
            startActivity(intent);
        });

        TextView textViewName = findViewById(R.id.userName);
        textViewName.setText(name);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.logs);


        Bundle bundle = new Bundle();
        bundle.putString("Key", key);
        bundle.putString("Chosen", hardwareId);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LogsFragment logsFragment = new LogsFragment();
        logsFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, logsFragment);
        fragmentTransaction.commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, new LogsFragment()).commit();

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId())
                {
                    case R.id.chat:
                        intent = new Intent(Logs.this, Chat.class);
                        intent.putExtra("Chosen", hardwareId);
                        intent.putExtra("Key", key);
                        intent.putExtra("Name", name);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.logs:
                        return true;
                    case R.id.home:
                        intent = new Intent(Logs.this, Home.class);
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
}