package com.example.vkr1;

import com.example.vkr1.Entity.Computer;
import com.example.vkr1.Entity.Computers;
import com.example.vkr1.Entity.LogsMany;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceholder {

    @GET("computers")
    Call<Computers> getComputers(@Query("api_key") String key);

    @GET("computer")
    Call<Computer> getComputer(@Query("hardware_id") String hardwareId, @Query("api_key") String api);

    @GET("log")
    Call<LogsMany> getAllLogs(@Query("hardware_id") String hardwareId, @Query("api_key") String api);

    @GET("log")
    Call<LogsMany> getSpecificLogs(@Query("hardware_id") String hardwareId, @Query("api_key") String api, @Query("type") int type,
                                   @Query("from") Long from, @Query("to") Long to);
}

