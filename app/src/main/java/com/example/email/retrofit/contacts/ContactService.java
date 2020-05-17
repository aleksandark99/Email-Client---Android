package com.example.email.retrofit.contacts;

import com.example.email.model.Contact;
import com.example.email.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContactService {

    @POST("contacts/{userId}")
    Call<Integer> addContact(@Body Contact newContact, @Path("userId") Integer userId, @Header("Authorization") String authToken);

    @GET("user_contacts/{id}")
    Call<List<Contact>> getAllContactsForUser(@Path("id") Integer idUser, @Header("Authorization") String authToken);

    @GET("contacts/{id}")
    Call<Contact> getContact(@Path("id") Integer idContact);

    @PUT("contacts")
    Call<Void> updateContact(@Body Contact contact, @Header("Authorization") String authToken);

    @DELETE("contacts/{id}")
    Call<Void> deleteContact(@Path("id") int idContact);

}
