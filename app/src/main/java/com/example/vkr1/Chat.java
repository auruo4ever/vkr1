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
import com.example.vkr1.Entity.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    private String currenttime;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

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


    }

    private void attemptSend() {

        String message = getMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Date date = new Date();
        currenttime = simpleDateFormat.format(calendar.getTime());
        Message message1 = new Message(message, key, date.getTime(), currenttime, true);
        messagesArrayList.add(message1);
        messagesAdapter.notifyDataSetChanged();

        mSocket.connect();
        if (mSocket.connected()) {

            getMessage.setText("");
            mSocket.on("my_response", onNewMessage);

            //типа тут?
            mSocket.emit("my_broadcast_event", message);
        }
        else {
            Log.e(TAG, "NO CONNECTION!!!! ");
        }

    }

    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String text;
                    String id;
                    try {
                        text = data.getString("data");

                        //ЗАМЕНИТЬ
                        Date date = new Date();
                        currenttime = simpleDateFormat.format(calendar.getTime());
                        Message message1 = new Message(text, key, date.getTime(), currenttime, false);
                        messagesArrayList.add(message1);
                        messagesAdapter.notifyDataSetChanged();

                        Log.e(TAG, "message: " + text);

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