package com.example.nh;

import java.util.List;

import io.reactivex.Completable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ExampleRepository {

    @POST("/user/chatting-room-users")
    Completable sendRestEcho(@Query("msg") String message);

}
