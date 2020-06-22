package com.example.email.retrofit.message;


import com.example.email.model.Message;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MessageService {

    @POST("messages/send/{idAccount}")
    Call<Boolean> sendNewMessage(@Body Message newMessage, @Path("idAccount") Integer idAccount, @Header("Authorization") String authToken);

    @GET("/messages/{account_id}")
    Call<Set<Message>> getAllMessages(@Path("account_id") int idAccount, @Header("Authorization") String authToken);

    @GET("/messagesfromback/{account_id}")
    Call<Set<Message>> getAllMessagesFromBack(@Path("account_id") int idAccount, @Header("Authorization") String authToken);

    @PUT("/messages/")
    Call<Boolean> makeMessageRead(@Body Message messageToBeMadeUnreadFalse,@Header("Authorization") String authToken);

    @PUT("/messages/delete")
    Call<Boolean> deleteMessage(@Body Message messageToBeMadeUnreadFalse,@Header("Authorization") String authToken);
}