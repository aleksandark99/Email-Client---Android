package com.example.email.retrofit.message;

import com.example.email.model.Contact;
import com.example.email.model.Message;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {

    @POST("messages/send/{idUser}/{idAccount}")
    Call<Boolean> sendNewMessage(@Body Message newMessage, @Path("idUser") Integer userId, @Path("idAccount") Integer idAccount, @Header("Authorization") String authToken);

}
