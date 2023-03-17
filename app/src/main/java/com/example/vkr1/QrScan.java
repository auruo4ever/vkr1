package com.example.vkr1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class QrScan extends AppCompatActivity {

    private EditText mInputMessageView;
    private TextView textView;
    private String key = "";
    Socket mSocket = null;
    SharedPreferences sPref;

    final String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scan);

        Button qr_btn = findViewById(R.id.btn_scan);
        qr_btn.setOnClickListener(v -> {
            scanCode();
        });

        Button btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(v -> {
            sPref = getSharedPreferences("Saved_key", MODE_PRIVATE);
            key = sPref.getString(SAVED_TEXT, "");

            Intent intent = new Intent(QrScan.this, MainActivity.class);
            intent.putExtra("Key", key);
            startActivity(intent);
        });

        mInputMessageView = findViewById(R.id.editText);

        Button btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(v -> {
            attemptSend();
        });

        textView = findViewById(R.id.textView);


        try {
            String uri = "http://afire.tech:5000?api_key=" + key;
            //String uri = "http://10.0.0.2:5000?api_key=" + "test_api_key";
            mSocket = IO.socket(uri);
        } catch (URISyntaxException e) {
            Log.e(TAG, "NO CONNECTION WEBSOCKETS ");
        }
        mSocket.connect();
    }

    private void scanCode() {


        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to set flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        //если что-то ОТСКАНИРОВАЛОСЬ
        String text = result.getContents();
        if (text != null) {
            key = text;


            sPref = getSharedPreferences("Saved_key", MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(SAVED_TEXT, key);
            ed.commit();
            Toast.makeText(QrScan.this, "Text saved", Toast.LENGTH_SHORT).show();

            /*
            //ПОДКЛЮЧЕНИЕ
            try {
                //String uri = "http://afire.tech:5000?api_key=" + key;
                String uri = "http://10.0.0.2:5000?api_key=" + "test_api_key";
                mSocket = IO.socket(uri);
            } catch (URISyntaxException e) {
                Log.e(TAG, "NO CONNECTION WEBSOCKETS ");
            }

             */
            


        }
    });

    private void attemptSend() {

        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (mSocket.connected()) {
            //mSocket.emit("user_connections");
            //mSocket.on("user_connections", onUserConnection);

            mSocket.on("my_response", onNewMessage);
            mInputMessageView.setText("");

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
                    try {
                        text = data.getString("data");
                        textView.setText(text);

                        Log.e(TAG, "run: " + text);

                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

    Emitter.Listener onUserConnection = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String text;
                    try {
                        text = data.toString();
                        textView.setText(text);

                        Log.e(TAG, "USER CONNECTION: " + text);

                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

}
