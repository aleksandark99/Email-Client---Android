package com.example.email.retrofit.folders;

import com.example.email.model.Folder;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FolderService {

    @GET("/folders/{account_id}")
    Call<Set<Folder>> getFoldersByAccount(@Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @POST("/folder/{account_id}")
    Call<Folder> createFolder(@Body Folder folder, @Path("account_id") int acc_id, @Header("Authorization") String authToken);
}
