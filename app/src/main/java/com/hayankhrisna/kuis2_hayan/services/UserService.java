package com.hayankhrisna.kuis2_hayan.services;


import com.hayankhrisna.kuis2_hayan.models.Envelope;
import com.hayankhrisna.kuis2_hayan.models.User;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UserService {

	@GET("/v1/users")
	Call<Envelope<List<User>>> getUsers(@Query("q") String q, @Query("page") int page, @Query("pageSize") int pageSize);

	@POST("/v1/users")
	@Headers("No-Authentication: true")
	Call<Envelope<User>> createUser(@Body User user);
}
