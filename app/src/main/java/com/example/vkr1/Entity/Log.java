package com.example.vkr1.Entity;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Log {

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private int logType;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("data")
    private LogData data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public LogData getData() {
        return data;
    }

    public void setData(LogData data) {
        this.data = data;
    }

    public String getNormalDate() {
        Date date = new java.util.Date((long) timestamp * 1000);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public String getLogTypeName() {
        LogType[] logTypeArray = LogType.values();
        String str = logTypeArray[logType].toString();
        return str;
    }
    public String getLogTypeFirstLetter() {
        LogType[] logTypeArray = LogType.values();
        String str = logTypeArray[logType].toString().substring(0, 1);
        return str;
    }

    public String getLogText() {
        String str = "";
        if (logType == 0) {
            for (String process : data.getProcesses()) {
                str = str + process + " ";
            }
            return str;
        }
        if  (logType == 1) {
            return "need to implement";
        }
        if  (logType == 2) {
            return "need to implement";
        }
        if  (logType == 3) {
            return "need to implement";
        }
        if  (logType == 4) {
            return "need to implement";
        }
        if  (logType == 5) {
            for (String brow : data.getBrowserHistory()) {
                str = str + brow + " ";
            }
            return str;
        }
        else return "null";
    }
}
