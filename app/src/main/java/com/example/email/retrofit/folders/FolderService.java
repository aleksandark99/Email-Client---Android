package com.example.email.retrofit.folders;

import com.example.email.model.Folder;

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

public interface FolderService {

    @GET("/folders/{account_id}")
    Call<Set<Folder>> getFoldersByAccount(@Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @GET("/some_folders/{account_id}/{message_id}")
    Call<Set<Folder>> getFoldersForChecking(@Path("account_id") int acc_id, @Path("message_id") int message_id, @Header("Authorization") String authToken);

    @GET("/subfolders/{acc_id}/{parent_folder}")
    Call<Set<Folder>> getSubFoldersByAccount(@Path("acc_id") int acc_id, @Path("parent_folder") int parent, @Header("Authorization") String authToken);

    @POST("/folder/{account_id}")
    Call<Folder> createFolder(@Body Folder folder, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @PUT("/folder/{account_id}")
    Call<Folder> updateFolder(@Body Folder folder, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @DELETE("folder/{folder_id}/{account_id}")
    Call<ResponseBody> deleteFolder(@Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);
}
