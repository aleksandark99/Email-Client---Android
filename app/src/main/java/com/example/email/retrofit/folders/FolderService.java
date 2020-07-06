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

    @GET("/{user_id}/folders/{account_id}")
    Call<Set<Folder>> getFoldersByAccount(@Path("user_id") int user_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @GET("/{user_id}/some_folders/{account_id}/{message_id}")
    Call<Set<Folder>> getFoldersForChecking(@Path("user_id") int user_id, @Path("account_id") int acc_id, @Path("message_id") int message_id, @Header("Authorization") String authToken);

    @GET("/{user_id}/subfolders/{acc_id}/{parent_folder}")
    Call<Set<Folder>> getSubFoldersByAccount(@Path("user_id") int user_id, @Path("acc_id") int acc_id, @Path("parent_folder") int parent, @Header("Authorization") String authToken);

    @POST("/{user_id}/folder/{account_id}")
    Call<Folder> createFolder(@Path("user_id") int user_id, @Body Folder folder, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @PUT("/{user_id}/folder/{account_id}")
    Call<Folder> updateFolder(@Path("user_id") int user_id, @Body Folder folder, @Path("account_id") int acc_id, @Header("Authorization") String authToken);

    @DELETE("/{user_id}/folder/{folder_id}/{account_id}")
    Call<ResponseBody> deleteFolder(@Path("user_id") int user_id, @Path("folder_id") int folder_id, @Path("account_id") int acc_id, @Header("Authorization") String authToken);
}
