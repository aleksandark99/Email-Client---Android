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

    @GET("/rules_operation/{folder_id}/{operation}")
    Call<Set<Rule>> getRulesByOperation(@Path("folder_id") int folder_id, @Path("operation") int operation, @Header("Authorization") String authToken);

    @GET("/rules/{folder_id}/{acc_id}")
    Call<Set<Rule>> getRulesByFolder(@Path("folder_id") int folder_id, @Path("acc_id") int acc_id, @Header("Authorization") String authToken);

    @POST("/rule/{folder_id}/{acc_id}")
    Call<Rule> createRule(@Body Rule rule, @Path("folder_id") int folder_id, @Path("acc_id") int acc_id, @Header("Authorization") String authToken);

    @DELETE("/rule/{rule_id}/{folder_id}/{acc_id}")
    Call<ResponseBody> deleteRule(@Path("rule_id") int rule_id, @Path("folder_id") int folder_id, @Path("acc_id") int acc_id, @Header("Authorization") String authToken);
}
