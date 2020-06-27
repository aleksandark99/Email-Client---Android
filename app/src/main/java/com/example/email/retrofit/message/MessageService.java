package com.example.email.retrofit.message;


import com.example.email.model.Message;

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

    @POST("messages/send/{idAccount}")
    Call<Boolean> sendNewMessage(@Body Message newMessage, @Path("idAccount") Integer idAccount, @Header("Authorization") String authToken);

    @GET("/messages/{account_id}")
    Call<Set<Message>> getAllMessages(@Path("account_id") int idAccount, @Header("Authorization") String authToken);

    @GET("/messagesfromback/{account_id}")
    Call<Set<Message>> getAllMessagesFromBack(@Path("account_id") int idAccount, @Header("Authorization") String authToken);

    @PUT("/messages/")
    Call<Boolean> makeMessageRead(@Body Message messageToBeMadeUnreadFalse,@Header("Authorization") String authToken);

    @PUT("/messages/addTag/{tagId}")
    Call<Boolean> addTagToMessage(@Path("tagId") int tagId,@Body Message message,@Header("Authorization") String authToken);

    @PUT("/messages/removeTag/{tagId}")
    Call<Boolean> removeTagToMessage(@Path("tagId") int tagId,@Body Message message,@Header("Authorization") String authToken);

    @GET("/messages/{folder_id}/{account_id}")
    Call<Set<Message>> getAllMessagesByRules(@Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @GET("/inactive_messages/{account_id}")
    Call<Set<Message>> getAllInactiveMessages(@Path("account_id") int account_id, @Header("Authorization") String authToken);

    @GET("/sent_messages/{account_id}")
    Call<Set<Message>> getAllSentMessages(@Path("account_id") int account_id, @Header("Authorization") String authToken);

    @GET("/drafts_messages/{account_id}")
    Call<Set<Message>> getAllDraftsMessages(@Path("account_id") int account_id, @Header("Authorization") String authToken);

    @PUT("/messages/delete")
    Call<Boolean> deleteMessage(@Body Message messageToBeMadeUnreadFalse,@Header("Authorization") String authToken);

    @DELETE("/message/delete/{message_id}")
    Call<ResponseBody> deleteMessagePhysically(@Path("message_id") int message_id, @Header("Authorization") String authToken);

    @PUT("/message/{message_id}/{folder_id}/{account_id}")
    Call<Message> moveMessageToFolder(@Path("message_id") int message_id, @Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @POST("message/{message_id}/{folder_id}/{account_id}/copy")
    Call<ResponseBody> copyMessageToFolder(@Path("message_id") int message_id, @Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);


    @POST("/message/drafts/{account_id}")
    Call<ResponseBody> moveMessageToDraft(@Path("account_id") int account_id,@Body Message message,  @Header("Authorization") String authToken);


}