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

    @GET("/{user_id}/messages/{folder_id}/{account_id}")
    Call<Set<Message>> getAllMessagesByRules(@Path("user_id") int user_id, @Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @GET("/{user_id}/inactive_messages/{account_id}")
    Call<Set<Message>> getAllInactiveMessages(@Path("user_id") int user_id, @Path("account_id") int account_id, @Header("Authorization") String authToken);

    @GET("/{user_id}/sent_messages/{account_id}")
    Call<Set<Message>> getAllSentMessages(@Path("user_id") int user_id, @Path("account_id") int account_id, @Header("Authorization") String authToken);

    @GET("/{user_id}/drafts_messages/{account_id}")
    Call<Set<Message>> getAllDraftsMessages(@Path("user_id") int user_id, @Path("account_id") int account_id, @Header("Authorization") String authToken);

    @PUT("/{user_id}/messages/delete")
    Call<Boolean> deleteMessage(@Path("user_id") int user_id,@Body Message messageToBeMadeUnreadFalse,@Header("Authorization") String authToken);

    @DELETE("/{user_id}/message/delete/{message_id}")
    Call<ResponseBody> deleteMessagePhysically(@Path("user_id") int user_id, @Path("message_id") int message_id, @Header("Authorization") String authToken);

    @PUT("/{user_id}/message/{message_id}/{folder_id}/{account_id}")
    Call<Message> moveMessageToFolder(@Path("user_id") int user_id, @Path("message_id") int message_id, @Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @POST("/{user_id}/message/{message_id}/{folder_id}/{account_id}/copy")
    Call<ResponseBody> copyMessageToFolder(@Path("user_id") int user_id, @Path("message_id") int message_id, @Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);


    @POST("/{user_id}/message/drafts/{account_id}")
    Call<ResponseBody> moveMessageToDraft(@Path("user_id") int user_id, @Path("account_id") int account_id,@Body Message message,  @Header("Authorization") String authToken);


}