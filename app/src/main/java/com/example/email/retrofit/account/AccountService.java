package com.example.email.retrofit.account;

import com.example.email.model.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountService {

    @POST("users/{idUser}/accounts")
    Call<Account> addAccount(@Body Account newAccount, @Path("idUser") Integer userId, @Header("Authorization") String authToken);

}
