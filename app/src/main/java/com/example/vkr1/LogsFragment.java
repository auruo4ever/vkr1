package com.example.vkr1;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vkr1.Adapters.LogsAdapter;
import com.example.vkr1.Entity.Log;
import com.example.vkr1.Entity.LogsMany;

import java.util.ArrayList;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LogsFragment extends Fragment {

    private long fromDate = 0L;
    private long toDate = 216426447400L;
    private int logNum = -1;
    private String key;
    private String hardwareId;
    private LogsMany logs;

    private String link = "http://afire.tech:5000/api/";
    Socket mSocket = null;
    ArrayList<Log> logsArrayList = new ArrayList<>();

    RecyclerView logsRecyclerView;
    LogsAdapter logsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        Socket mSocket = null;

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.keySet().size() > 3) {
                fromDate = bundle.getLong("fromDate");
                toDate = bundle.getLong("toDate");
                logNum = bundle.getInt("logNum");
                key = bundle.getString("Key");
                hardwareId = bundle.getString("Chosen");
            }
            else {
                key = bundle.getString("Key");
                hardwareId = bundle.getString("Chosen");
            }
        }

        //Кнопка настроек
        Button btn_next = (Button) view.findViewById(R.id.settings);
        btn_next.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            bundle1.putString("Key", key);
            bundle1.putString("Chosen", hardwareId);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            SettingsFragment settingsFragment = new SettingsFragment();
            settingsFragment.setArguments(bundle1);

            fragmentTransaction.replace(R.id.container, settingsFragment);
            fragmentTransaction.commit();

        });


        //Подключение к апи
        Retrofit retrofit = new Retrofit.Builder().baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<LogsMany> call;
        if (logNum >= 0) {
            call = jsonPlaceholder.getSpecificLogs(hardwareId, key, logNum, fromDate, toDate);
        }
        else {
            call = jsonPlaceholder.getAllLogs(hardwareId, key);
        }
        call.enqueue(new Callback<LogsMany>() {
            @Override
            public void onResponse(Call<LogsMany> call, Response<LogsMany> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                logs = response.body();

                initRecyclerView(logs, view);
            }
            @Override
            public void onFailure(Call<LogsMany> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                android.util.Log.e("e", "RESPONSE  " + t.getMessage());
            }
        });

        return view;
    }

    private void initRecyclerView (LogsMany logsMany, View view) {
        //Adapter
        logsArrayList = logsMany.getLogs();
        logsRecyclerView = view.findViewById(R.id.recyclerview);
        logsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        logsRecyclerView.setLayoutManager(linearLayoutManager);
        logsAdapter = new LogsAdapter(logsArrayList, view.getContext());
        logsRecyclerView.setAdapter(logsAdapter);
    }

}