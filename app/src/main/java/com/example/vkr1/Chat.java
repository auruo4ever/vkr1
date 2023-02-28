package com.example.vkr1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Socket mSocket = null;
        TextView textViewName = findViewById(R.id.hardware_id);

        //Получение intend
        String hardwareId;
        String key;
        String name;
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

        textViewName.setText(name);


        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.chat);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId())
                {
                    case R.id.home:
                        intent = new Intent(Chat.this, Home.class);
                        intent.putExtra("Chosen", hardwareId);
                        intent.putExtra("Key", key);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.chat:
                        return true;
                    case R.id.about:
                        intent = new Intent(Chat.this, About.class);
                        intent.putExtra("Chosen", hardwareId);
                        intent.putExtra("Key", key);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



        //ПОДКЛЮЧЕНИЕ
        try {
            String uri = "http://afire.tech:5000?api_key=" + key;
            mSocket = IO.socket(uri);
        } catch (URISyntaxException e) {
            Log.e(TAG, "NO CONNECTION WEBSOCKETS ");
        }

    }
}