package com.example.email.retrofit.message;


import com.example.email.model.Message;
import com.example.email.repository.Repository;

import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MessageService {



    @POST("/{user_id}/messages/send/{idAccount}")
    Call<Boolean> sendNewMessage(@Path("user_id") int user_id,@Body Message newMessage, @Path("idAccount") Integer idAccount, @Header("Authorization") String authToken);

    @GET("/{user_id}/messages/{account_id}")
    Call<Set<Message>> getAllMessages(@Path("user_id") int user_id,@Path("account_id") int idAccount, @Header("Authorization") String authToken);

    @GET("/{user_id}/messagesfromback/{account_id}")
    Call<Set<Message>> getAllMessagesFromBack(@Path("user_id") int user_id,@Path("account_id") int idAccount, @Header("Authorization") String authToken);

    @PUT("/{user_id}/messages/")
    Call<Boolean> makeMessageRead(@Path("user_id") int user_id,@Body Message messageToBeMadeUnreadFalse,@Header("Authorization") String authToken);

    @PUT("/{user_id}/messages/addTag/{tagId}")
    Call<Boolean> addTagToMessage(@Path("user_id") int user_id,@Path("tagId") int tagId,@Body Message message,@Header("Authorization") String authToken);

    @PUT("/{user_id}/messages/removeTag/{tagId}")
    Call<Boolean> removeTagToMessage(@Path("user_id") int user_id,@Path("tagId") int tagId,@Body Message message,@Header("Authorization") String authToken);

    @GET("/messages/{folder_id}/{account_id}")
    Call<Set<Message>> getAllMessagesByRules(@Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @GET("/inactive_messages/{account_id}")
    Call<Set<Message>> getAllInactiveMessages(@Path("account_id") int account_id, @Header("Authorization") String authToken);

    @GET("/sent_messages/{account_id}")
    Call<Set<Message>> getAllSentMessages(@Path("account_id") int account_id, @Header("Authorization") String authToken);

    @GET("/drafts_messages/{account_id}")
    Call<Set<Message>> getAllDraftsMessages(@Path("account_id") int account_id, @Header("Authorization") String authToken);

    @PUT("/{user_id}/messages/delete")
    Call<Boolean> deleteMessage(@Path("user_id") int user_id,@Body Message messageToBeMadeUnreadFalse,@Header("Authorization") String authToken);

    @DELETE("/message/delete/{message_id}")
    Call<ResponseBody> deleteMessagePhysically(@Path("message_id") int message_id, @Header("Authorization") String authToken);

    @PUT("/message/{message_id}/{folder_id}/{account_id}")
    Call<Message> moveMessageToFolder(@Path("message_id") int message_id, @Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @POST("message/{message_id}/{folder_id}/{account_id}/copy")
    Call<ResponseBody> copyMessageToFolder(@Path("message_id") int message_id, @Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);


    @POST("/message/drafts/{account_id}")
    Call<ResponseBody> moveMessageToDraft(@Path("account_id") int account_id,@Body Message message,  @Header("Authorization") String authToken);


}