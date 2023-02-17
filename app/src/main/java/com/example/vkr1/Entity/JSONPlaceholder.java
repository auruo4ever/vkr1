package com.example.vkr1.Entity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceholder {

    @GET("computers")
    Call<Computers> getComputers();

    @GET("computer")
    Call<Computer> getComputer(@Query("hardware_id") String hardwareId);
}
