package com.example.email.retrofit.rules;

import com.example.email.model.Rule;

import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RuleService {

    @GET("/{user_id}/rules_operation/{folder_id}/{operation}")
    Call<Set<Rule>> getRulesByOperation(@Path("user_id") int user_id, @Path("folder_id") int folder_id, @Path("operation") int operation, @Header("Authorization") String authToken);

    @GET("/{user_id}/rules/{folder_id}/{acc_id}")
    Call<Set<Rule>> getRulesByFolder(@Path("user_id") int user_id, @Path("folder_id") int folder_id, @Path("acc_id") int acc_id, @Header("Authorization") String authToken);

    @POST("/{user_id}/rule/{folder_id}/{acc_id}")
    Call<Rule> createRule(@Path("user_id") int user_id, @Body Rule rule, @Path("folder_id") int folder_id, @Path("acc_id") int acc_id, @Header("Authorization") String authToken);

    @DELETE("/{user_id}/rule/{rule_id}/{folder_id}/{acc_id}")
    Call<ResponseBody> deleteRule(@Path("user_id") int user_id, @Path("rule_id") int rule_id, @Path("folder_id") int folder_id, @Path("acc_id") int acc_id, @Header("Authorization") String authToken);
}
