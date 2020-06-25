package com.example.email.retrofit.tags;

import com.example.email.model.Message;
import com.example.email.model.Tag;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TagsService {

    @POST("tags/{userId}")
    Call<Tag> addNewTag(@Body Tag newTag, @Path("userId") Integer idAccount, @Header("Authorization") String authToken);

    @DELETE("/tags/{tagId}")
    Call<ResponseBody> deleteTag(@Path("tagId") Integer tagId, @Header("Authorization") String authToken);


}
