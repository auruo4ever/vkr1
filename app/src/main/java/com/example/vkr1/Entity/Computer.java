package com.example.vkr1.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Computer {

    @SerializedName("name")
    private String name;

    @SerializedName("hardware_id")
    private String hardwareId;

    @SerializedName("cpu")
    private String cpu;

    @SerializedName("gpus")
    private List<String> gpus;

    @SerializedName("ram")
    private Integer ram;

    @SerializedName("disks")
    private List<String> disks;

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

    public List<String> getGpus() {
        return gpus;
    }

    public void setGpus(List<String> gpus) {
        this.gpus = gpus;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public List<String> getDisks() {
        return disks;
    }

    public void setDisks(List<String> disks) {
        this.disks = disks;
    }

}
