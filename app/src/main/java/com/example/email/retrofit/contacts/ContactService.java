package com.example.email.retrofit.contacts;

import com.example.email.model.Contact;
import com.example.email.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContactService {

    @POST("contacts/{idUser}")
    Call<Contact> addContact(@Body Contact newContact, @Path("idUser") Integer userId, @Header("Authorization") String authToken);

    @GET("users/{idUser}/contacts")
    Call<Set<Contact>> getAllContactsForUser(@Path("idUser") Integer idUser, @Header("Authorization") String authToken);

    @GET("contacts/{id}")
    Call<Contact> getContact(@Path("id") Integer idContact);

    @PUT("users/{idUser}/contacts")
    Call<Contact> updateContact(@Body Contact contact, @Path("idUser") Integer idUser, @Header("Authorization") String authToken);

    //@HTTP(method = "DELETE", path = "{login}", hasBody = true)
    @DELETE("contacts/{userId}/{contactId}")
    Call<HTTP> deleteContact(@Path("userId") Integer userId, @Path("contactId") Integer contactId, @Header("Authorization") String authToken);

}
