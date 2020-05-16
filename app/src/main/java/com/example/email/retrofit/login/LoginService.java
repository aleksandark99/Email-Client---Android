package com.example.email.retrofit.login;

import com.example.email.model.Contact;
import com.example.email.model.Login;
import com.example.email.model.LoginResponse;
import com.example.email.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {

    @POST("login")
    Call<LoginResponse> testLogin(@Body Login login);
}
