package com.example.email.retrofit.account;

import com.example.email.model.Account;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccountService {

    @POST("users/{idUser}/accounts")
    Call<Account> addAccount(@Body Account newAccount, @Path("idUser") Integer userId, @Header("Authorization") String authToken);

    @PUT("users/{idUser}/accounts/{idAccount}")
    Call<Account> updateAccount(@Body Account account, @Path("idUser") Integer idUser, @Path("idAccount") Integer idAccount, @Header("Authorization") String authToken);

    @DELETE("/users/{idUser}/accounts/{idAccount}")
    Call<ResponseBody> deleteAccount(@Path("idUser") Integer userId, @Path("idAccount") Integer idAccount, @Header("Authorization") String authToken);


}
