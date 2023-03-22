package com.example.vkr1.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LogData {

    @SerializedName("processes")
    private ArrayList<String> processes;

    @SerializedName("browser_history")
    private ArrayList<String> browserHistory;

    public ArrayList<String> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<String> processes) {
        this.processes = processes;
    }

    public ArrayList<String> getBrowserHistory() {
        return browserHistory;
    }

    public void setBrowserHistory(ArrayList<String> browserHistory) {
        this.browserHistory = browserHistory;
    }


    @Override
    public String toString() {
        String str = "Processes: ";
        for (String pr : processes) {
            str = str + " " + processes + " ";
        }
        return str;
    }
}
