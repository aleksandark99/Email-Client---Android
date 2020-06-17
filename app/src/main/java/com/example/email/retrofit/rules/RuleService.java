package com.example.email.retrofit.rules;

import com.example.email.model.Rule;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface RuleService {

    @GET("/rules_operation/{folder_id}/{operation}")
    Call<Set<Rule>> getRulesByOperation(@Path("folder_id") int folder_id, @Path("operation") int operation, @Header("Authorization") String authToken);
}
