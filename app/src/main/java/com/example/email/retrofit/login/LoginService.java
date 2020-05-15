package com.example.email.retrofit.login;

import com.example.email.model.Contact;
import com.example.email.model.Login;
import com.example.email.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {

    @POST("login")
    Call<ResponseBody> testLogin(@Body Login login);
}
