package com.example.email.retrofit.contacts;

import com.example.email.model.Contact;

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

    @POST("contacts")
    Call<Integer> addContact(@Body Contact newContact);

    @GET("contacts")
    Call<List<Contact>> getAllContacts();

    @GET("contacts/{id}")
    Call<Contact> getContact(@Path("id") Integer idContact);

    @PUT("contacts")
    Call<Void> updateContact(@Body Contact contact);

    @DELETE("contacts/{id}")
    Call<Void> deleteContact(@Path("id") int idContact);
}
