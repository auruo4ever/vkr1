package com.example.vkr1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.vkr1.Entity.LogType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class SettingsFragment extends Fragment {

    private DatePickerDialog dateFrom;
    private Button dateFromButton;
    private DatePickerDialog dateTo;
    private Button dateToButton;
    private Button okButton;
    View view;
    int flag = 0;
    private Calendar fromCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    private Calendar toCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    long fromDate = 0, toDate = 0;
    private int logNum = -1;
    String[] countries = { "Бразилия", "Аргентина", "Колумбия", "Чили", "Уругвай"};
    private String key;
    private String hardwareId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            key = bundle.getString("Key");
            hardwareId = bundle.getString("Chosen");
        }

        //ВЫБОР ДВУХ ДАТ
        initDatePicker();

        dateFromButton = (Button) view.findViewById(R.id.dateFrom);
        dateFromButton.setText(getTodaysDate());
        dateFromButton.setOnClickListener(v -> {
            flag = 1;
            dateFrom.show();
        });
        dateToButton = (Button) view.findViewById(R.id.dateTo);
        dateToButton.setText(getTodaysDate());
        dateToButton.setOnClickListener(v -> {
            flag = 2;
            dateTo.show();
        });
        fromDate = 0L;
        toDate = Calendar.getInstance().getTimeInMillis() / 1000;


        //НАЖАТИЕ ОК
        okButton = (Button) view.findViewById(R.id.ok);
        okButton.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            bundle1.putLong("fromDate", fromDate);
            bundle1.putLong("toDate", toDate);
            bundle1.putInt("logNum", logNum);
            bundle1.putString("Key", key);
            bundle1.putString("Chosen", hardwareId);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            LogsFragment logsFragment = new LogsFragment();
            logsFragment.setArguments(bundle1);

            fragmentTransaction.replace(R.id.container, logsFragment);
            fragmentTransaction.commit();
        });



        //ТИП ЛОГА В СПИННЕРЕ
        Spinner spinner =  (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<LogType> adapter = new ArrayAdapter<LogType>(getActivity(), R.layout.spinner_item, LogType.values());

        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = parent.getItemAtPosition(position).toString();
                logNum = position;
                //Log.e("SPIN ME BABY", item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        return view;
    }


    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    private void initDatePicker()
    {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                if (flag == 1) {
                    dateFromButton.setText(date);
                    fromCal.set(year, month, day);
                    fromDate = fromCal.getTimeInMillis() / 1000;
                }
                else {
                    dateToButton.setText(date);
                    toCal.set(year, month, day);
                    toDate = toCal.getTimeInMillis() / 1000;
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        dateFrom = new DatePickerDialog(getActivity(), style, dateSetListener , year, month, day);
        dateFrom.getDatePicker().setMaxDate(System.currentTimeMillis());

        dateTo = new DatePickerDialog(getActivity(), style, dateSetListener , year, month, day);
        dateTo.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }



}