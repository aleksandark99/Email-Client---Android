package com.example.email.retrofit.register;

import com.example.email.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterService {


    @POST("register")
    Call<Boolean> registerUser(@Body User potentiallyNewUser);
}
