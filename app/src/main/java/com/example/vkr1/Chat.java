package com.example.vkr1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vkr1.Adapters.MessagesAdapter;
import com.example.vkr1.Entity.Computer;
import com.example.vkr1.Entity.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Chat extends AppCompatActivity {
    Socket mSocket = null;
    private String key;
    private String hardwareId;
    private String name;

    private EditText getMessage;
    private ImageButton sendMessage;

    MessagesAdapter messagesAdapter;
    ArrayList<Message> messagesArrayList;

    RecyclerView messageRecyclerView;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    String jsonMessage = "";

    String room;
    int from;
    String msg;
    long timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Socket mSocket = null;
        TextView textViewName = findViewById(R.id.userName);
        getMessage = findViewById(R.id.getmessage);
        sendMessage = findViewById(R.id.imageviewsendmessage);


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
        room = hardwareId + "_chat";

        //Имя компьютера сверху
        textViewName.setText(name);

        //Кнопка назад
        ImageButton backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(Chat.this, MainActivity.class);
            intent.putExtra("Key", key);
            startActivity(intent);
        });

        //Adapter
        messagesArrayList=new ArrayList<>();
        messageRecyclerView = findViewById(R.id.recyclerviewofspecific);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(Chat.this,messagesArrayList);
        messageRecyclerView.setAdapter(messagesAdapter);


        //Открыть вебсокет
        createWebSocketClient();

        //Дата
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();
            }
        });



        //Нижняя навигация
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.chat);

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
                        intent.putExtra("Name", name);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.chat:
                        return true;
                    case R.id.logs:
                        intent = new Intent(Chat.this, Logs.class);
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

    private void createWebSocketClient() {
        try {
            String uri = "http://afire.tech:5000?api_key=" + key;
            mSocket = IO.socket(uri);
        } catch (URISyntaxException e) {
            Log.e(TAG, "NO CONNECTION WEBSOCKETS ");
        }
        mSocket.connect();
        while(!mSocket.connected()) {
        }
        if (mSocket.connected()) {
            try {
                jsonMessage = new JSONObject()
                        .put("room", room)
                        .put("offset", 0)
                        .toString();
            } catch (Exception ex) {
                Log.e(TAG, "NO CONNECTION WEBSOCKETS ");
            }

            mSocket.on("chat_join", chatJoin);
            mSocket.emit("chat_history", jsonMessage);
            mSocket.on("chat_history", chatHistory);
        }
        else {
            Log.e("NO CONN", "no connection");
        }
        messagesAdapter.notifyDataSetChanged();
    }

    private void attemptSend() {
        String message = getMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        long unixTime = System.currentTimeMillis() / 1000L;
        //ID добавить
        Message message1 = new Message(message, 12, unixTime, true);
        messagesArrayList.add(message1);
        messagesAdapter.notifyDataSetChanged();

        if (mSocket.connected()) {
            getMessage.setText("");
            try {
                jsonMessage = new JSONObject()
                        .put("from", message1.getSenderId())
                        .put("room", room)
                        .put("timestamp", message1.getTimestamp())
                        .put("msg", message1.getMessage())
                        .toString();
            } catch (Exception ex) {
                Log.e("ERROR", ex.getMessage());
            }
            mSocket.emit("chat_message", jsonMessage);
            mSocket.on("chat_message", chatMessage);
        }
        else {
            Log.e(TAG, "NO CONNECTION!!!! ");
        }

    }

    Emitter.Listener chatJoin = new Emitter.Listener() {
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
                        room = data.getString("room");

                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

    Emitter.Listener chatMessage = new Emitter.Listener() {
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
                        from = data.getInt("from");
                        timestamp = data.getLong("timestamp");
                        msg = data.getString("msg");
                        Log.e("PRISHEL", "room: " + room);


                        //long unixTime = System.currentTimeMillis() / 1000L;
                        Message message = new Message(msg, from , timestamp, false);
                        messagesArrayList.add(message);
                        messagesAdapter.notifyDataSetChanged();

                        Log.e(TAG, "message: " + message);



                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

    Emitter.Listener chatHistory = new Emitter.Listener() {
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
                        JSONArray messages = data.getJSONArray("messages");
                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject mesObj = messages.getJSONObject(i);
                            from = mesObj.getInt("from");
                            timestamp = mesObj.getLong("timestamp");
                            msg = mesObj.getString("msg");

                            Message message = new Message(msg, from , timestamp, false);
                            messagesArrayList.add(message);
                        }
                        messagesAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}