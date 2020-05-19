package com.example.email.retrofit.user;

import com.example.email.model.LoginResponse;
import com.example.email.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {


    @PUT("users/")
    Call<LoginResponse> updateUser(@Body User user, @Header("Authorization") String authToken);

}
