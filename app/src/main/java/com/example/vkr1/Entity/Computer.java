package com.example.vkr1.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Computer {

    @SerializedName("name")
    private String name;

    @SerializedName("hardware_id")
    private String hardwareId;

    @SerializedName("cpu")
    private String cpu;

    @SerializedName("gpus")
    private ArrayList<String> gpus;

    @SerializedName("ram")
    private long ram;

    @SerializedName("disks")
    private ArrayList<String> disks;

    @SerializedName("OS")
    private String OS;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHardware_id() {
        return hardwareId;
    }

    public void setHardware_id(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public ArrayList<String> getGpus() {
        return gpus;
    }

    public void setGpus(ArrayList<String> gpus) {
        this.gpus = gpus;
    }

    public long getRam() {
        return ram;
    }

    public void setRam(long ram) {
        this.ram = ram;
    }

    public ArrayList<String> getDisks() {
        return disks;
    }

    public void setDisks(ArrayList<String> disks) {
        this.disks = disks;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }
}
