package com.example.vkr1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.vkr1.Adapters.MainRecyclerAdapter;
import com.example.vkr1.Entity.Computers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
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
    Socket mSocket = null;
    String jsonMessage = "";
    int myID;
    String token;

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




        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://afire.tech:5000/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                                .build();
        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<Computers> call = jsonPlaceholder.getComputers(key);
        call.enqueue(new Callback<Computers>() {
            @Override
            public void onResponse(Call<Computers> call, Response<Computers> response) {
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
                        token = task.getResult();
                    }
                });

        //Открыть вебсокет
        createWebSocketClient();

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

    private void createWebSocketClient() {
        try {
            String uri = "http://afire.tech:5000?api_key=" + key;
            mSocket = IO.socket(uri);
        } catch (URISyntaxException e) {
            Log.e(MotionEffect.TAG, "NO CONNECTION WEBSOCKETS ");
        }
        mSocket.connect();

        while(!mSocket.connected()) {

        }
        if (mSocket.connected()) {
            mSocket.emit("client_info");
            mSocket.on("client_info", clientInfo);
        }
        else {
            Log.e("NO CONN", "no connection");
        }
    }
    Emitter.Listener clientInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = new JSONObject();
                    try {
                        String dataString = (String) args[0];
                        data = new JSONObject(dataString);
                    } catch (Exception ex) {
                        Log.e("ERROR", ex.getMessage());
                    }
                    try {
                        myID = data.getInt("id");
                        Log.e("My ID", String.valueOf(myID));
                        jsonMessage = new JSONObject()
                                .put("operator_id", myID)
                                .put("token", token)
                                .toString();
                        mSocket.emit("mobile_token", jsonMessage);
                        Log.e("JSON", jsonMessage);
                    }
                    catch (Exception e) {
                        return;
                    }
                }
            });

        }
    };


} 