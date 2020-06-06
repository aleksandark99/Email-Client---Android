package com.example.email.retrofit.message;


import com.example.email.model.Message;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {

    @POST("messages/send/{idAccount}")
    Call<Boolean> sendNewMessage(@Body Message newMessage, @Path("idAccount") Integer idAccount, @Header("Authorization") String authToken);

}
